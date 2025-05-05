# preprocessing.py
from PIL import Image
from torchvision import transforms
import requests
from io import BytesIO
def load_image_from_url(url: str) -> Image.Image:
    response = requests.get(url)
    return Image.open(BytesIO(response.content)).convert('RGB')

def preprocess(image: Image.Image, device):
    transform = transforms.ToTensor()
    img_tensor = transform(image).unsqueeze(0).to(device)
    return img_tensor