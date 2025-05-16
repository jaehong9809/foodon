from fastapi import APIRouter
from ..schemas.request import RequestSchema
from ..schemas.response import ResponseSchema
from ..service.preprocessing import load_image_from_url, preprocess
from ..service.postprocessing import postprocess
from ..core.model_state import model
import torch
import time
from PIL import Image
router = APIRouter()

device = torch.device("cuda" if torch.cuda.is_available() else "cpu")


@router.post("/detect", response_model=ResponseSchema)
async def detect_objects(request: RequestSchema):
    total_start = time.time()
    # 이미지 로딩
    t0 = time.time()
    image = load_image_from_url(request.url)
    t1 = time.time()
    print(f"🕒 이미지 로딩 시간: {t1 - t0:.4f}초")

    width, height = image.size

    # 전처리
    t2 = time.time()
    img_tensor = preprocess(image, device)
    t3 = time.time()
    print(f"🧹 전처리 시간: {t3 - t2:.4f}초")

    # 추론
    t4 = time.time()
    with torch.inference_mode():
        prediction = model(img_tensor)[0]
    t5 = time.time()
    print(f"🤖 추론 시간: {t5 - t4:.4f}초")

    # 후처리
    t6 = time.time()
    food_items = postprocess(prediction, width, height, model_input_size=640)
    t7 = time.time()
    print(f"🔍 후처리 시간: {t7 - t6:.4f}초")

    total_end = time.time()
    print(f"⏱️ 전체 처리 시간: {total_end - total_start:.4f}초")

    return ResponseSchema(food=food_items)

from fastapi import UploadFile, File

@router.post("/detect2", response_model=ResponseSchema)
async def detect_objects_2(file: UploadFile = File(...)):
    total_start = time.time()
    
    # 이미지 로딩
    t0 = time.time()
    image = Image.open(file.file).convert("RGB")  # PIL 이미지로 변환
    t1 = time.time()
    print(f"🕒 모델 로드, 이미지 로딩 시간: {t1 - t0:.4f}초")

    width, height = image.size

    # 전처리
    t2 = time.time()
    img_tensor = preprocess(image, device)
    t3 = time.time()
    print(f"🧹 전처리 시간: {t3 - t2:.4f}초")

    # 추론
    t4 = time.time()
    with torch.inference_mode():
        prediction = model(img_tensor)[0]
    t5 = time.time()
    print(f"🤖 추론 시간: {t5 - t4:.4f}초")

    # 후처리
    t6 = time.time()
    food_items = postprocess(prediction, width, height, model_input_size=640)
    t7 = time.time()
    print(f"🔍 후처리 시간: {t7 - t6:.4f}초")

    total_end = time.time()
    print(f"⏱️ 전체 처리 시간: {total_end - total_start:.4f}초")

    return ResponseSchema(food=food_items)
