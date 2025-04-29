from fastapi import APIRouter
from ..schemas.request import RequestSchema
from ..schemas.response import ResponseSchema
from ..service.preprocessing import load_image_from_url, preprocess
from ..service.postprocessing import postprocess
from ..core.model_loader import load_model
import torch

router = APIRouter()

device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
model = load_model()

@router.post("/detect", response_model=ResponseSchema)
async def detect_objects(request: RequestSchema):
    image = load_image_from_url(request.url)
    width, height = image.size

    img_tensor = preprocess(image, device)

    with torch.inference_mode():
        prediction = model(img_tensor)[0]

    food_items = postprocess(prediction, width, height)

    return ResponseSchema(food=food_items)
