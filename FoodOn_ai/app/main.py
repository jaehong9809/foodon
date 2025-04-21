from typing import Union
from fastapi import FastAPI
from .schemas.response import ResponseSchema, FoodItem, Position
from .schemas.request import RequestSchema
from .core.coco_dataset_name import COCO_CATEGORIES
from PIL import Image
import requests
from torchvision import models, transforms
from torchvision.models.detection import (
    fasterrcnn_resnet50_fpn,
    FasterRCNN_ResNet50_FPN_Weights,
)
import torch
from collections import defaultdict
from contextlib import asynccontextmanager


@asynccontextmanager
async def lifespan(app: FastAPI):
    print("✅ FastAPI 앱 시작됨 (lifespan)")
    yield
    print("🛑 FastAPI 앱 종료됨 (lifespan)")


app = FastAPI(root_path="/ai", lifespan=lifespan)

device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
weights = FasterRCNN_ResNet50_FPN_Weights.DEFAULT
model = fasterrcnn_resnet50_fpn(weights=weights)
model.to(device)
model.eval()


@app.get("/")
def read_root():
    return {"Hello": "FoodOn"}


@app.post("/detect", response_model=ResponseSchema)
async def detect_objects(request: RequestSchema):
    # 이미지 로드
    image = Image.open(requests.get(request.url, stream=True).raw).convert("RGB")
    width, height = image.size

    # 전처리
    transform = transforms.Compose([transforms.ToTensor()])
    img_tensor = transform(image).unsqueeze(0).to(device)

    # 추론
    with torch.no_grad():
        prediction = model(img_tensor)[0]

    boxes = prediction['boxes']
    scores = prediction['scores']
    labels = prediction['labels']

    # 결과 저장
    detected = defaultdict(lambda: {"count": 0, "positions": []})

    for i in range(len(scores)):
        if scores[i] > 0.8:
            class_id = labels[i].item()
            label = COCO_CATEGORIES.get(class_id)
            if not label:
                continue

            x1, y1, x2, y2 = boxes[i].tolist()
            box = {
                "x": x1 / width,
                "y": y1 / height,
                "width": (x2 - x1) / width,
                "height": (y2 - y1) / height
            }

            detected[label]["count"] += 1
            detected[label]["positions"].append(Position(**box))

    # 응답 데이터 생성
    food_items = [
        FoodItem(name=k, count=v["count"], positions=v["positions"])
        for k, v in detected.items()
    ]

    return ResponseSchema(food=food_items)