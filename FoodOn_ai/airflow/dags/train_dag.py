from airflow import DAG
from airflow.operators.python import PythonOperator
from datetime import datetime, timedelta
import mlflow
import mlflow.pytorch
import torch
import torch.nn as nn
import torch.optim as optim
import shutil
import os
import logging

from data_loader import load_data_from_db, delete_data
from train.train import train_and_log_with_mlflow


# 기본 설정
default_args = {
    'owner': 'airflow',
    'retries': 1,
    'retry_delay': timedelta(minutes=5),
}

# Airflow 로그에 남기기 위한 로거 객체
logger = logging.getLogger(__name__)

def train_log_save_model():
    logger.info("✅ [시작] 모델 학습 준비 시작")

    # 데이터 로딩
    df = load_data_from_db(min_count=10)
    if df is None or df.empty:
        logger.warning("⚠️ [중단] 데이터 부족으로 학습 건너뜀")
        return

    logger.info("✅ [조건만족] 모델 학습 조건 만족")
    
    train_and_log_with_mlflow()

    delete_data()
    

with DAG(
    dag_id='train_register_and_save_model',
    default_args=default_args,
    start_date=datetime(2025, 4, 28),
    schedule_interval='*/20 * * * *',  # 매주 월요일 00:00
    catchup=False,
    tags=['ml', 'mlflow', 'shared_model'],
) as dag:

    train_and_save = PythonOperator(
        task_id='train_log_save_model',
        python_callable=train_log_save_model,
    )

    train_and_save