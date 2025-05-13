import os
import mlflow
import mlflow.pytorch
from .names_id import names, class_id_to_index
from .transform import get_transform
from .FoodDetectionDataset import FoodDetectionDataset
# MLflow 설정

from torch.utils.data import Dataset
from PIL import Image, ImageOps
import torch
import json
import numpy as np
from pathlib import Path
import torchvision.transforms as T

from torchvision.models.detection import (
    fasterrcnn_resnet50_fpn,
    FasterRCNN_ResNet50_FPN_Weights,
)
from torchvision.models.detection.faster_rcnn import FastRCNNPredictor

mlflow.set_tracking_uri("http://mlflow_server:5000")
mlflow.set_experiment("food_detection")


def create_model(num_classes: int = 68):
    weights = FasterRCNN_ResNet50_FPN_Weights.DEFAULT
    model = fasterrcnn_resnet50_fpn(weights=weights)

    # for param in model.backbone.body.parameters():
    #     param.requires_grad = False
    for name, param in model.backbone.body.named_parameters():
        if "layer3" in name or "layer4" in name:
            param.requires_grad = True

    in_features = model.roi_heads.box_predictor.cls_score.in_features
    model.roi_heads.box_predictor = FastRCNNPredictor(in_features, num_classes)
    return model


import torch.optim as optim
from tqdm import tqdm


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

        # ✅ 현재 GPU 메모리 사용량(MB)도 출력
        mem_allocated = torch.cuda.memory_allocated(device) / 1024**2
        loop.set_postfix(loss=losses.item(), gpu_mem=f"{mem_allocated:.1f}MB")

    return total_loss


from torchmetrics.detection.mean_ap import MeanAveragePrecision


@torch.no_grad()
def evaluate(model, data_loader, device):
    model.eval()
    metric = MeanAveragePrecision(iou_thresholds=[0.5])

    for images, targets in data_loader:
        images = [img.to(device) for img in images]
        targets = [{k: v.to(device) for k, v in t.items()} for t in targets]
        outputs = model(images)
        metric.update(preds=outputs, target=targets)

    return metric.compute()  # 전체 결과 통째로 반환


full_dataset = FoodDetectionDataset("./dataset", transforms=get_transform(train=True))

# 80:20 비율로 train/val 나누기
train_size = int(0.8 * len(full_dataset))
val_size = len(full_dataset) - train_size
train_dataset, val_dataset = random_split(
    full_dataset,
    [train_size, val_size],
    generator=torch.Generator().manual_seed(42),  # 재현 가능하게 시드 고정
)

# 검증용 transform 따로 지정
val_dataset.dataset.transforms = get_transform(train=False)

# DataLoader 설정
train_loader = DataLoader(
    train_dataset,
    batch_size=16,
    shuffle=True,
    num_workers=1,
    pin_memory=True,
    collate_fn=lambda x: tuple(zip(*x)),
)

val_loader = DataLoader(
    val_dataset,
    batch_size=16,
    shuffle=False,
    num_workers=1,
    pin_memory=True,
    collate_fn=lambda x: tuple(zip(*x)),
)

import os
import torch
import time

# 모델 설정
device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
model = create_model(num_classes=68)
model.to(device)

# 🔽 이어서 학습을 위한 가중치 로드
base_dir = os.path.dirname(os.path.abspath(__file__))
model_path = os.path.join(base_dir, "best_model_0513.pth")
if os.path.exists(model_path):
    model.load_state_dict(torch.load(model_path))
    print(f"✅ 사전 학습된 가중치를 로드했습니다: {model_path}")
else:
    print("⚠️ 사전 학습된 가중치 파일이 없습니다. 새로운 모델로 학습을 시작합니다.")

optimizer = optim.SGD(
    [p for p in model.parameters() if p.requires_grad],
    lr=0.005,
    momentum=0.9,
    weight_decay=0.0005,
)

# 모델 저장 디렉토리
#save_dir = "weights5"
#os.makedirs(save_dir, exist_ok=True)

# Early Stopping 설정
patience = 5  # 개선 없을 때 몇 번 참을지
best_val_map = 0.0
patience_counter = 0
num_epochs = 10  # 최대 에폭 수

with mlflow.start_run(run_name="fasterrcnn_training"):
    mlflow.log_param("num_epochs", num_epochs)
    mlflow.log_param("optimizer", "SGD")
    mlflow.log_param("learning_rate", 0.005)

    total_start_time = time.time()
    for epoch in range(num_epochs):
        start_time = time.time()

        train_loss = train_one_epoch(model, train_loader, device, optimizer)
        result = evaluate(model, val_loader, device)
        val_map = result["map_50"].item()

        elapsed = time.time() - start_time

        print(
            f"[Epoch {epoch+1}] 🏋️ Train Loss: {train_loss:.4f} | 📊 Val mAP@0.5: {val_map:.4f} | ⏱ Time: {elapsed:.2f}s"
        )
        mlflow.log_metric("val_map@0.5", val_map, step=epoch)
        mlflow.log_metric("train_loss", train_loss, step=epoch)

        if val_map > best_val_map:
            best_val_map = val_map
            patience_counter = 0
            #save_path = os.path.join(save_dir, "best_model.pth")
            #torch.save(model.state_dict(), save_path)
            print(f"💾 모델 저장 (val mAP 감지): {save_path}")

            # MLflow 모델 저장
            mlflow.pytorch.log_model(model, "model")

        else:
            patience_counter += 1
            print(f"⏳ patience: {patience_counter}/{patience}")

            if patience_counter >= patience:
                print(f"🚩 Early stopping: {epoch+1} epoch 이후 val mAP 감지 없음")
                break

    total_elapsed = time.time() - total_start_time
    print(f"\n⏱ 전체 학습 시간: {total_elapsed:.2f}초 ({total_elapsed/60:.2f}분)")
