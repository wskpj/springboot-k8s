#!/bin/bash
set -e

readonly WORK_DIR=$(cd "$(dirname "$0")/../.." && pwd)
cd "${WORK_DIR}"

echo "[1/4] Tearing down all K8s resources..."
kustomize build --enable-helm ./k8s/envs/stage | kubectl delete -f - --ignore-not-found

echo "[2/4] Building Docker Image..."
docker build -t springboot-app:latest .

echo "[3/4] Tagging and Pushing to Registry..."
docker tag springboot-app:latest localhost:5001/springboot-app:latest
docker push localhost:5001/springboot-app:latest

echo "[4/4] Applying K8s Manifests via Kustomize..."
kustomize build --enable-helm ./k8s/envs/stage | kubectl apply -f -

