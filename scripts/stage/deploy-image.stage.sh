#!/bin/bash
set -e

readonly WORK_DIR=$(cd "$(dirname "$0")/../.." && pwd)
cd "${WORK_DIR}"

echo "[1/4] Building Docker Image..."
docker build -t springboot-app:latest .

echo "[2/4] Loading Image to Kind Cluster..."
kind load docker-image springboot-app:latest

echo "[3/4] Applying K8s Manifests via Kustomize (ConfigMap, Secrets, Patches)..."
kustomize build --enable-helm . | kubectl apply -f -

echo "[4/4] Restarting Spring Boot Pods to apply new code..."
kubectl rollout restart deployment springboot-app-stage
