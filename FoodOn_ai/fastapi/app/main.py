from fastapi import FastAPI
from contextlib import asynccontextmanager
from .router import detect, detect_yolo
from .core.model_loader import start_scheduler


@asynccontextmanager
async def lifespan(app: FastAPI):
    print("✅ FastAPI 앱 시작됨 (lifespan)")
    start_scheduler()
    yield
    print("🛑 FastAPI 앱 종료됨 (lifespan)")

app = FastAPI(root_path="/ai", lifespan=lifespan)

app.include_router(detect.router)
app.include_router(detect_yolo.router)

@app.get("/")
def read_root():
    return {"Hello": "FoodOn"}
