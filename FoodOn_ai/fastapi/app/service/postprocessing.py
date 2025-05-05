from collections import defaultdict
from ..schemas.response import FoodItem, Position
from ..core.coco_dataset_name import COCO_CATEGORIES

from collections import defaultdict
from ..core.food_dataset_name import names  # int형 key일 경우엔 문자열로 처리 필요

def postprocess(prediction, orig_w, orig_h, threshold=0.7, model_input_size=512):
    
    boxes = prediction['boxes']
    scores = prediction['scores']
    labels = prediction['labels']

    scale_x = orig_w / model_input_size
    scale_y = orig_h / model_input_size

    detected = defaultdict(lambda: {"count": 0, "positions": []})

    for i in range(len(scores)):
        if scores[i] > threshold:
            label_id = labels[i].item()
            food_name = names.get(label_id)
            if not food_name:
                continue

            x1, y1, x2, y2 = boxes[i].tolist()

            # ✅ 원본 사이즈 기준으로 복원
            x1 *= scale_x
            x2 *= scale_x
            y1 *= scale_y
            y2 *= scale_y

            box = {
                "x": x1 / orig_w,
                "y": y1 / orig_h,
                "width": (x2 - x1) / orig_w,
                "height": (y2 - y1) / orig_h
            }


            detected[food_name]["count"] += 1
            detected[food_name]["positions"].append(Position(**box))

    return [
        FoodItem(name=k, count=v["count"], positions=v["positions"])
        for k, v in detected.items()
    ]



# def postprocess(prediction, width, height, threshold=0.8):
#     boxes = prediction['boxes']
#     scores = prediction['scores']
#     labels = prediction['labels']

#     detected = defaultdict(lambda: {"count": 0, "positions": []})

#     for i in range(len(scores)):
#         if scores[i] > threshold:
#             class_id = labels[i].item()
#             label = COCO_CATEGORIES.get(class_id)
#             if not label:
#                 continue

#             x1, y1, x2, y2 = boxes[i].tolist()
#             box = {
#                 "x": x1 / width,
#                 "y": y1 / height,
#                 "width": (x2 - x1) / width,
#                 "height": (y2 - y1) / height
#             }

#             detected[label]["count"] += 1
#             detected[label]["positions"].append(Position(**box))

#     food_items = [
#         FoodItem(name=k, count=v["count"], positions=v["positions"])
#         for k, v in detected.items()
#     ]

#     return food_items
