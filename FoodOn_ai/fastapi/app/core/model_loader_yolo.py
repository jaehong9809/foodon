import torch
import os
import pathlib
import platform
if platform.system() == "Windows":
    pathlib.PosixPath = pathlib.WindowsPath
def load_model():
    model = torch.hub.load('ultralytics/yolov5', 'custom', path='app/core/best.pt', force_reload=True)
    model.conf = 0.25  # optional threshold
    model.eval()
    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
    model.to(device)
    return model, device
