from pydantic import BaseModel, HttpUrl
from typing import List

# 요청 스키마
class RequestSchema(BaseModel):
    url: HttpUrl
