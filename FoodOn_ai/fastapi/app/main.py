from fastapi import FastAPI
from contextlib import asynccontextmanager
from .router import detect

@asynccontextmanager
async def lifespan(app: FastAPI):
    print("✅ FastAPI 앱 시작됨 (lifespan)")
    yield
    print("🛑 FastAPI 앱 종료됨 (lifespan)")

app = FastAPI(root_path="/ai", lifespan=lifespan)

app.include_router(detect.router)

@app.get("/")
def read_root():
    return {"Hello": "FoodOn"}
