
import albumentations as A
from albumentations.pytorch import ToTensorV2

def get_transform(train: bool):
    if train:
        return A.Compose([
            A.ColorJitter(brightness=0.2, contrast=0.2, saturation=0.2, hue=0.02, p=0.5),
            A.HorizontalFlip(p=0.5),
            A.Affine(rotate=0, translate_percent=(0.05, 0.05), scale=(0.95, 1.05), p=0.5),
            A.Resize(640, 640),  # torchvision처럼 강제 resize
            A.Normalize(mean=(0.485, 0.456, 0.406), std=(0.229, 0.224, 0.225)),
            ToTensorV2(),
        ], bbox_params=A.BboxParams(format='pascal_voc', label_fields=['category_ids']))
    else:
        return A.Compose([
            A.Resize(640, 640),
            A.Normalize(mean=(0.485, 0.456, 0.406), std=(0.229, 0.224, 0.225)),
            ToTensorV2(),
        ], bbox_params=A.BboxParams(format='pascal_voc', label_fields=['category_ids']))