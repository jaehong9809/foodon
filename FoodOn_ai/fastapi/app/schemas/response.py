from pydantic import BaseModel
from typing import List

# 감지된 객체의 위치 및 신뢰도 정보
class Position(BaseModel):
    x: float         # 왼쪽 상단 X (정규화 비율)
    y: float         # 왼쪽 상단 Y (정규화 비율)
    width: float     # 정규화된 너비
    height: float    # 정규화된 높이
    confidence: float  # 객체 탐지 confidence 점수 (0~1)

# 음식 종류별로 count 및 위치 목록
class FoodItem(BaseModel):
    name: str
    count: int
    positions: List[Position]

# 최종 응답
class ResponseSchema(BaseModel):
    food: List[FoodItem]
