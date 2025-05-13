import os
import json
import pymysql
import pandas as pd
import requests
import logging
from PIL import Image
from io import BytesIO
from dotenv import load_dotenv
import os

load_dotenv()
# 로거 설정
logger = logging.getLogger(__name__)
logger.setLevel(logging.INFO)


def generate_dataset_from_df(
    df, image_dir="../train/dataset/images", label_dir="../train/dataset/labels"
):
    os.makedirs(image_dir, exist_ok=True)
    os.makedirs(label_dir, exist_ok=True)

    for meal_id, group in df.groupby("meal_id"):
        image_url = group.iloc[0]["meal_image"]
        image_path = os.path.join(image_dir, f"{meal_id}.jpg")

        try:
            response = requests.get(image_url, timeout=5)
            image = Image.open(BytesIO(response.content)).convert("RGB")
            image.save(image_path)
            width, height = image.size
            logger.info(f"✅ 이미지 저장 완료: {image_path}")
        except Exception as e:
            logger.warning(f"⚠️ 이미지 다운로드 실패 ({meal_id}): {e}")
            continue

        annotations = []
        for _, row in group.iterrows():
            abs_x = int(row["x"] * width)
            abs_y = int(row["y"] * height)
            abs_w = int(row["width"] * width)
            abs_h = int(row["height"] * height)

            annotations.append(
                {
                    "x": abs_x,
                    "y": abs_y,
                    "width": abs_w,
                    "height": abs_h,
                    "class_id": row["food_name"],
                }
            )

        label = {
            "image": f"{meal_id}.jpg",
            "width": width,
            "height": height,
            "annotations": annotations,
        }

        label_path = os.path.join(label_dir, f"{meal_id}.json")
        with open(label_path, "w", encoding="utf-8") as f:
            json.dump(label, f, ensure_ascii=False, indent=2)

        logger.info(f"📄 라벨 저장 완료: {label_path}")


def load_data_from_db(min_count=100):
    logger.info("📦 DB 연결 시도 중...")

    conn = pymysql.connect(
        host=os.getenv("DB_HOST"),
        port=int(os.getenv("DB_PORT")),
        user=os.getenv("DB_USER"),
        password=os.getenv("DB_PASSWORD"),
        database=os.getenv("DB_NAME"),
        charset=os.getenv("DB_CHARSET"),
        ssl_disabled=True,
    )

    try:
        with conn.cursor() as cursor:
            cursor.execute("SELECT COUNT(*) FROM meals")
            count = cursor.fetchone()[0]
            logger.info(f"📊 현재 meals 수: {count}건")

        if count < min_count:
            logger.warning(f"⚠️ 데이터 건수 : {count}")
            logger.warning(f"⚠️ 데이터 부족으로 중단 (기준: {min_count})")
            return None

        logger.info("✅ 충분한 데이터 확보, 데이터 불러오는 중...")

        query = """
        SELECT 
            m.meal_id AS meal_id,
            m.meal_image,
            mi.food_name,
            mi.x, mi.y, mi.width, mi.height
        FROM meals m
        JOIN meal_items mi ON m.meal_id = mi.meal_id
        ORDER BY m.meal_id
        """
        df = pd.read_sql(query, conn)

        logger.info(f"📥 DB에서 {len(df)}개의 라벨 데이터를 성공적으로 불러왔습니다")

        # 데이터셋 생성
        generate_dataset_from_df(df)

        logger.info("📂 이미지 및 라벨 파일 저장 완료")
        return df

    except Exception as e:
        logger.error(f"❌ DB 처리 중 오류 발생: {e}")
        return None

    finally:
        conn.close()
        logger.info("🔌 DB 연결 종료")
