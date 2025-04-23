from pydantic import BaseModel
from typing import List, Dict

# 응답 스키마
class Position(BaseModel):
    x: float  # 비율 (0~1)
    y: float
    width: float
    height: float


class FoodItem(BaseModel):
    name: str
    count: int
    positions: List[Position]


class ResponseSchema(BaseModel):
    food: List[FoodItem]
