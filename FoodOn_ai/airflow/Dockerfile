FROM apache/airflow:2.7.2-python3.10

COPY requirements.txt /
RUN pip install --no-cache-dir -r /requirements.txt

USER root
RUN apt-get update && apt-get install -y docker.io \
    && groupadd -f docker \
    && usermod -aG docker airflow
USER airflow

COPY dags/ /opt/airflow/dags/
