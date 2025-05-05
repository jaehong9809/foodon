from PIL import Image, ImageOps
from torchvision import transforms
import requests
import torch

# def load_image_from_url(url: str) -> Image.Image:
#     return Image.open(requests.get(url, stream=True).raw).convert("RGB")

# def preprocess(image: Image.Image, device):
#     transform = transforms.Compose([transforms.ToTensor()])
#     img_tensor = transform(image).unsqueeze(0).to(device)
#     return img_tensor

def load_image_from_url(url: str) -> Image.Image:
    image = Image.open(requests.get(url, stream=True).raw).convert("RGB")
    return ImageOps.exif_transpose(image)

def preprocess(image: Image.Image, device):
    transform = transforms.Compose([
        transforms.Resize((512, 512)),
        transforms.ToTensor(),
        transforms.Normalize(mean=[0.485, 0.456, 0.406],
                             std=[0.229, 0.224, 0.225])
    ])
    img_tensor = transform(image).unsqueeze(0).to(device)
    return img_tensor