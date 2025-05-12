import torch
from torchvision.models.detection import (
    fasterrcnn_resnet50_fpn,
    FasterRCNN_ResNet50_FPN_Weights,
)
from torchvision.models.detection.faster_rcnn import FastRCNNPredictor
import json
from .food_dataset_name import names
import os


device = torch.device("cuda" if torch.cuda.is_available() else "cpu")


def create_model(num_classes: int):
    weights = FasterRCNN_ResNet50_FPN_Weights.DEFAULT
    model = fasterrcnn_resnet50_fpn(weights=weights)

    # layer4만 학습되도록 설정
    for name, param in model.backbone.body.named_parameters():
        param.requires_grad = "layer4" in name

    # 기존 box predictor를 새로 교체
    in_features = model.roi_heads.box_predictor.cls_score.in_features
    model.roi_heads.box_predictor = FastRCNNPredictor(in_features, num_classes)

    return model


def load_model():
    base_dir = os.path.dirname(os.path.abspath(__file__))
    model_path = os.path.join(base_dir, "best_model.pth")
    num_classes = len(names)  # names = {0: ..., 1: ..., ...}

    model = create_model(num_classes)
    model.load_state_dict(torch.load(model_path, map_location=device))
    model.to(device)
    model.eval()
    return model
