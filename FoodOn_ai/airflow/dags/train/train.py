import os
import time
import json
import torch
import mlflow
import mlflow.pytorch
import numpy as np
from tqdm import tqdm
from pathlib import Path
from PIL import Image, ImageOps
from torch import optim
from torch.utils.data import DataLoader, random_split
from torchmetrics.detection.mean_ap import MeanAveragePrecision
from torchvision.models.detection import fasterrcnn_resnet50_fpn, FasterRCNN_ResNet50_FPN_Weights
from torchvision.models.detection.faster_rcnn import FastRCNNPredictor

from .names_id import names, class_id_to_index
from .transform import get_transform
from .FoodDetectionDataset import FoodDetectionDataset
from mlflow.tracking import MlflowClient

def create_model(num_classes: int = 68):
    weights = FasterRCNN_ResNet50_FPN_Weights.DEFAULT
    model = fasterrcnn_resnet50_fpn(weights=weights)
    for name, param in model.backbone.body.named_parameters():
        if "layer3" in name or "layer4" in name:
            param.requires_grad = True

    in_features = model.roi_heads.box_predictor.cls_score.in_features
    model.roi_heads.box_predictor = FastRCNNPredictor(in_features, num_classes)
    return model


def train_one_epoch(model, data_loader, device, optimizer):
    model.train()
    total_loss = 0.0
    loop = tqdm(data_loader, desc="🔥 Training", leave=False)

    for images, targets in loop:
        images = [img.to(device) for img in images]
        targets = [{k: v.to(device) for k, v in t.items()} for t in targets]

        loss_dict = model(images, targets)
        losses = sum(loss for loss in loss_dict.values())

        optimizer.zero_grad()
        losses.backward()
        optimizer.step()

        total_loss += losses.item()
        mem_allocated = torch.cuda.memory_allocated(device) / 1024**2
        loop.set_postfix(loss=losses.item(), gpu_mem=f"{mem_allocated:.1f}MB")

    return total_loss


@torch.no_grad()
def evaluate(model, data_loader, device):
    model.eval()
    metric = MeanAveragePrecision(iou_thresholds=[0.5])

    for images, targets in data_loader:
        images = [img.to(device) for img in images]
        targets = [{k: v.to(device) for k, v in t.items()} for t in targets]
        outputs = model(images)
        metric.update(preds=outputs, target=targets)

    return metric.compute()

def collate_fn(batch):
    return tuple(zip(*batch))

def train_and_log_with_mlflow():
    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
    model = create_model(num_classes=68).to(device)
    print("모델 완성")
    # 데이터셋 구성
    full_dataset = FoodDetectionDataset("/dataset/dataset", transforms=get_transform(train=True))
    print("dataset완료")
    train_size = int(0.8 * len(full_dataset))
    val_size = len(full_dataset) - train_size
    train_dataset, val_dataset = random_split(full_dataset, [train_size, val_size], generator=torch.Generator().manual_seed(42))
    val_dataset.dataset.transforms = get_transform(train=False)
    train_loader = DataLoader(
        train_dataset,
        batch_size=1,
        shuffle=True,
        num_workers=0,        # CPU에서 안정적으로 동작하도록 설정
        pin_memory=False,     # pin_memory를 비활성화
        collate_fn=collate_fn,
    )

    val_loader = DataLoader(
        val_dataset,
        batch_size=1,
        shuffle=False,
        num_workers=0,        # CPU에서 안정적으로 동작하도록 설정
        pin_memory=False,     # pin_memory를 비활성화
        collate_fn=collate_fn,
    )

    # 사전 가중치 로드
    base_dir = os.path.dirname(os.path.abspath(__file__))
    model_path = os.path.join(base_dir, "best_model_0513.pth")
    map_location = torch.device("cuda") if torch.cuda.is_available() else torch.device("cpu")

    loaded = False

    try:
        # MLflow에서 Production 모델 로드 시도
        model = mlflow.pytorch.load_model("models:/food_detection/Production").to(device)
        print("✅ MLflow에서 Production 모델을 로드했습니다.")
        loaded = True
    except Exception as e:
        print(f"⚠️ MLflow 모델 로드 실패: {e}")
        if os.path.exists(model_path):
            try:
                model = create_model(num_classes=68).to(device)
                model.load_state_dict(torch.load(model_path, map_location=map_location))
                print(f"✅ 로컬 모델을 로드했습니다: {model_path}")
                loaded = True
            except Exception as e:
                print(f"🚨 로컬 모델 로딩 실패: {e}")

    if not loaded:
        print("🆕 사전 모델이 없어 새 모델로 학습을 시작합니다.")
        model = create_model(num_classes=68).to(device)

    optimizer = optim.SGD([p for p in model.parameters() if p.requires_grad], lr=0.005, momentum=0.9, weight_decay=0.0005)

    # 학습 설정
    patience = 5
    best_val_map = 0.0
    patience_counter = 0
    num_epochs = 10

    mlflow.set_tracking_uri("http://k12s203.p.ssafy.io:5000")  # URL 오타 수정
    mlflow.set_experiment("food_detection")

    with mlflow.start_run(run_name="fasterrcnn_training"):
        mlflow.log_params({
            "num_epochs": num_epochs,
            "optimizer": "SGD",
            "learning_rate": 0.005
        })

        total_start_time = time.time()

        for epoch in range(num_epochs):
            start_time = time.time()

            train_loss = train_one_epoch(model, train_loader, device, optimizer)
            result = evaluate(model, val_loader, device)
            val_map = result["map_50"].item()

            elapsed = time.time() - start_time
            print(f"[Epoch {epoch+1}] 🏋️ Train Loss: {train_loss:.4f} | 📊 Val mAP@0.5: {val_map:.4f} | ⏱ Time: {elapsed:.2f}s")

            mlflow.log_metric("train_loss", train_loss, step=epoch)
            mlflow.log_metric("val_map_0_5", val_map, step=epoch)

            if val_map > best_val_map:
                best_val_map = val_map
                patience_counter = 0

                # save_dir = "weights5"
                # os.makedirs(save_dir, exist_ok=True)
                # save_path = os.path.join(save_dir, "best_model.pth")
                # torch.save(model.state_dict(), save_path)
                # print(f"📀 모델 저장 (val mAP 향상): {save_path}")

                mlflow.pytorch.log_model(model, "model", registered_model_name="food_detection")
                model_uri = "runs:/" + mlflow.active_run().info.run_id + "/model"
                mlflow.register_model(model_uri, "food_detection")
                print("✅ 모델을 MLflow Model Registry에 등록했습니다.")
                client = MlflowClient()
                latest_versions = client.get_latest_versions("food_detection", stages=["None"])
                if latest_versions:
                    model_version = latest_versions[0].version
                    client.transition_model_version_stage(
                        name="food_detection",
                        version=model_version,
                        stage="Production",
                        archive_existing_versions=True,
                    )
                print(f"🚀 모델 버전 {model_version}을 'Production'으로 전환했습니다.")
            else:
                patience_counter += 1
                print(f"⏳ patience: {patience_counter}/{patience}")
                if patience_counter >= patience:
                    print(f"🚩 Early stopping: {epoch+1} epoch 이후 성능 향상 없음")
                    break

        total_elapsed = time.time() - total_start_time
        print(f"\n⏱ 전체 학습 시간: {total_elapsed:.2f}초 ({total_elapsed/60:.2f}분)")

