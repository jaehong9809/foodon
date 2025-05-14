import torch
from torchvision.models.detection import (
    fasterrcnn_resnet50_fpn,
    FasterRCNN_ResNet50_FPN_Weights,
)
from torchvision.models.detection.faster_rcnn import FastRCNNPredictor
import mlflow.pytorch
import os
from .food_dataset_name import names


device = torch.device("cuda" if torch.cuda.is_available() else "cpu")


def create_model(num_classes: int):
    weights = FasterRCNN_ResNet50_FPN_Weights.DEFAULT
    model = fasterrcnn_resnet50_fpn(weights=weights)

    for name, param in model.backbone.body.named_parameters():
        if "layer3" in name or "layer4" in name:
            param.requires_grad = True

    in_features = model.roi_heads.box_predictor.cls_score.in_features
    model.roi_heads.box_predictor = FastRCNNPredictor(in_features, num_classes)
    return model


def load_model():
    base_dir = os.path.dirname(os.path.abspath(__file__))
    model_path = os.path.join(base_dir, "best_model_0513.pth")
    num_classes = len(names)

    model = None

    try:
        # MLflow에서 Production 모델 먼저 시도
        model = mlflow.pytorch.load_model("models:/food_detection/Production").to(device)
        model.eval()
        print("✅ MLflow에서 모델 로드 완료")
        return model
    except Exception as e:
        print(f"⚠️ MLflow 로드 실패: {e}")

    # 로컬 fallback
    if os.path.exists(model_path):
        model = create_model(num_classes)
        model.load_state_dict(torch.load(model_path, map_location=device))
        model.to(device)
        model.eval()
        print(f"✅ 로컬 모델 로드 완료: {model_path}")
    else:
        raise FileNotFoundError("❌ 로컬 모델 파일도 존재하지 않습니다.")

    return model
