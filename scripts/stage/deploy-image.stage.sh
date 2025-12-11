#!/bin/bash
set -e

readonly WORK_DIR=$(cd "$(dirname "$0")/../.." && pwd)
cd "${WORK_DIR}"

echo "[Phase 1/2] Starting Teardown and Build in parallel..."

# 1. Teardown K8s resources (Background)
(
  echo "  [Teardown] Tearing down K8s resources..."
  kustomize build --enable-helm ./k8s/envs/stage | kubectl delete -f - --ignore-not-found
) &
TEARDOWN_PID=$!

# 2. Build Docker Image (Background)
(
  echo "  [Build] Building Docker Image..."
  docker build -t springboot-app:latest .
) &
BUILD_PID=$!

# Wait for both Phase 1 tasks to complete
wait $TEARDOWN_PID $BUILD_PID
echo "[Phase 1/2] Teardown and Build completed."

echo "[Phase 2/2] Starting Main Deployment Phase..."

echo "  [Deploy] Tagging and Pushing to Registry..."
docker tag springboot-app:latest localhost:5001/springboot-app:latest
docker push localhost:5001/springboot-app:latest

echo "  [Deploy] Applying K8s Manifests via Kustomize..."
kustomize build --enable-helm ./k8s/envs/stage | kubectl apply -f -

echo "Deployment finished successfully."
