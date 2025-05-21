import requests
from PIL import Image, ImageOps, UnidentifiedImageError
from io import BytesIO
import numpy as np
import albumentations as A
from albumentations.pytorch import ToTensorV2
import torch
# def load_image_from_url(url: str) -> Image.Image:
#     return Image.open(requests.get(url, stream=True).raw).convert("RGB")

# def preprocess(image: Image.Image, device):
#     transform = transforms.Compose([transforms.ToTensor()])
#     img_tensor = transform(image).unsqueeze(0).to(device)
#     return img_tensor

def load_image_from_url(url: str) -> Image.Image:
    response = requests.get(url, stream=True, timeout=5)
    
    if response.status_code != 200:
        raise ValueError(f"❌ 이미지 요청 실패: {response.status_code} - {url}")

    try:
        image = Image.open(BytesIO(response.content)).convert("RGB")
    except UnidentifiedImageError as e:
        raise ValueError(f"❌ 이미지 열기 실패 (형식 오류): {e} - {url}")

    return ImageOps.exif_transpose(image)

def preprocess(image: Image.Image, device):
    transform = A.Compose([
        A.Resize(640, 640),
        A.Normalize(mean=(0.485, 0.456, 0.406),
                    std=(0.229, 0.224, 0.225)),
        ToTensorV2(),
    ])
    image_np = np.array(image)
    transformed = transform(image=image_np)
    img_tensor = transformed["image"].unsqueeze(0).to(device)
    return img_tensor