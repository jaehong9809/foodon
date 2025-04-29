from PIL import Image
from torchvision import transforms
import requests
import torch

def load_image_from_url(url: str) -> Image.Image:
    return Image.open(requests.get(url, stream=True).raw).convert("RGB")

def preprocess(image: Image.Image, device):
    transform = transforms.Compose([transforms.ToTensor()])
    img_tensor = transform(image).unsqueeze(0).to(device)
    return img_tensor
