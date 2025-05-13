from torch.utils.data import Dataset
from PIL import Image, ImageOps
import torch
import json
import numpy as np
from pathlib import Path
import torchvision.transforms as T


class FoodDetectionDataset(Dataset):
    def __init__(self, root_dir: str, transforms=None):
        self.transforms = transforms
        self.items = []
        root = Path(root_dir)

        # 하위 폴더에서 이미지와 라벨을 수집
        for category_dir in root.glob("**/"):
            image_dir = category_dir / "images"
            label_dir = category_dir / "labels"
            if not (image_dir.exists() and label_dir.exists()):
                continue

            for image_path in image_dir.glob("*.jpg"):
                label_path = label_dir / f"{image_path.stem}.json"
                if label_path.exists():
                    self.items.append((image_path, label_path))

    def __getitem__(self, idx):
        image_path, label_path = self.items[idx]
        image = Image.open(image_path).convert("RGB")
        image = ImageOps.exif_transpose(image)

        with open(label_path) as f:
            data = json.load(f)

        anns = data["annotations"]
        bboxes = []
        category_ids = []

        for ann in anns:
            xmin = ann["x"]
            ymin = ann["y"]
            xmax = xmin + ann["width"]
            ymax = ymin + ann["height"]
            bboxes.append([xmin, ymin, xmax, ymax])
            class_name = ann["class_id"]
            category_ids.append(class_id_to_index[class_name])  # 반드시 외부에서 정의

        if self.transforms:
            transformed = self.transforms(
                image=np.array(image), bboxes=bboxes, category_ids=category_ids
            )
            image = transformed["image"]
            boxes = torch.tensor(transformed["bboxes"], dtype=torch.float32)
            labels = torch.tensor(transformed["category_ids"], dtype=torch.int64)
        else:
            image = T.ToTensor()(image)
            boxes = torch.tensor(bboxes, dtype=torch.float32)
            labels = torch.tensor(category_ids, dtype=torch.int64)

        target = {"boxes": boxes, "labels": labels, "image_id": torch.tensor([idx])}

        return image, target

    def __len__(self):
        return len(self.items)