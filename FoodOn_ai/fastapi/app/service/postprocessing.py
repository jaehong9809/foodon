from collections import defaultdict
from ..schemas.response import FoodItem, Position
from ..core.coco_dataset_name import COCO_CATEGORIES

def postprocess(prediction, width, height, threshold=0.8):
    boxes = prediction['boxes']
    scores = prediction['scores']
    labels = prediction['labels']

    detected = defaultdict(lambda: {"count": 0, "positions": []})

    for i in range(len(scores)):
        if scores[i] > threshold:
            class_id = labels[i].item()
            label = COCO_CATEGORIES.get(class_id)
            if not label:
                continue

            x1, y1, x2, y2 = boxes[i].tolist()
            box = {
                "x": x1 / width,
                "y": y1 / height,
                "width": (x2 - x1) / width,
                "height": (y2 - y1) / height
            }

            detected[label]["count"] += 1
            detected[label]["positions"].append(Position(**box))

    food_items = [
        FoodItem(name=k, count=v["count"], positions=v["positions"])
        for k, v in detected.items()
    ]

    return food_items
