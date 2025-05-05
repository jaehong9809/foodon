# router.py
from fastapi import APIRouter
from ..schemas.request import RequestSchema
from ..schemas.response import ResponseSchema
from ..service.preprocessing_yolo import load_image_from_url, preprocess
from ..service.postprocessing_yolo import postprocess
from ..core.model_loader_yolo import load_model
import torch
from fastapi import UploadFile, File
from PIL import Image
from io import BytesIO

model, device = load_model()
router = APIRouter()

@router.post("/detect-yolo", response_model=ResponseSchema)
async def detect_objects(request: RequestSchema):
    image = load_image_from_url(request.url)
    width, height = image.size

    with torch.no_grad():
        results = model(str(request.url))

    food_items = postprocess(results, width, height)
    return ResponseSchema(food=food_items)


@router.post("/detect-yolo2", response_model=ResponseSchema)
async def detect_objects(file: UploadFile = File(...)):
    # 이미지 로드
    contents = await file.read()
    image = Image.open(BytesIO(contents)).convert("RGB")
    width, height = image.size

    # 추론
    with torch.no_grad():
        results = model(image)

    # 후처리
    food_items = postprocess(results, width, height)
    return ResponseSchema(food=food_items)