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

# DAG 기본 설정
default_args = {
    'owner': 'airflow',
    'retries': 1,
    'retry_delay': timedelta(minutes=5),
}

# DAG 정의
with DAG(
    dag_id='train_register_and_save_model',
    default_args=default_args,
    start_date=datetime(2025, 4, 28),
    schedule_interval='*/10 * * * *',   # 10분마다
    catchup=False,
    tags=['ml', 'mlflow', 'shared_model'],
) as dag:

    def train_log_save_model():
        print("✅ 모델 학습 시작")

        # 모델 만들기
        model = nn.Linear(10, 2)
        criterion = nn.MSELoss()
        optimizer = optim.Adam(model.parameters(), lr=0.001)

        # 간단히 학습
        x = torch.randn(20, 10)
        y = torch.randn(20, 2)
        for epoch in range(3):
            optimizer.zero_grad()
            output = model(x)
            loss = criterion(output, y)
            loss.backward()
            optimizer.step()

        # MLflow 설정
        mlflow.set_tracking_uri("http://mlflow:5000")
        mlflow.set_experiment("foodon-training")

        # MLflow Run
        with mlflow.start_run() as run:
            mlflow.log_param("learning_rate", 0.001)
            mlflow.log_metric("loss", loss.item())

            # 모델 저장 경로
            artifact_path = "model"
            mlflow.pytorch.log_model(model, artifact_path=artifact_path)

            # 모델 Registry에 등록
            result = mlflow.register_model(
                model_uri=f"runs:/{run.info.run_id}/{artifact_path}",
                name="foodon-model"
            )
            print(f"✅ 모델 등록 완료: {result.name} (v{result.version})")

            # 추가! shared_model에 모델 복사
            local_model_dir = f"/mlflow/mlruns/{run.info.experiment_id}/{run.info.run_id}/artifacts/{artifact_path}"
            local_model_file = os.path.join(local_model_dir, "data", "model.pth")

            target_shared_model = "/shared_model/best_model.pth"

            # 파일 존재하면 복사
            if os.path.exists(local_model_file):
                os.makedirs("/shared_model", exist_ok=True)
                shutil.copy(local_model_file, target_shared_model)
                print(f"✅ shared_model에 모델 저장 완료: {target_shared_model}")
            else:
                print(f"⚠️ 모델 파일을 찾을 수 없습니다: {local_model_file}")

    # PythonOperator 등록
    train_and_save = PythonOperator(
        task_id='train_log_save_model',
        python_callable=train_log_save_model,
    )

    train_and_save
