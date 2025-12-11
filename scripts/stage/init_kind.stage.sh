#!/bin/bash
set -e

readonly WORK_DIR=$(cd "$(dirname "$0")/../.." && pwd)
cd "${WORK_DIR}"

# 1. Add NGINX Ingress Controller
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/deploy/static/provider/kind/deploy.yaml

# Add nodeSelector to Ingress Controller
kubectl patch deployment ingress-nginx-controller -n ingress-nginx --patch '{"spec": {"template": {"spec": {"nodeSelector": {"node-role.kubernetes.io/control-plane": ""}}}}}'

# 2. Deploy Infrastructure via Helm (Metrics Server)
helm upgrade --install kind-infra ./k8s/base/templates/kind-infra --namespace kube-system

# 3. Create namespace
kubectl create namespace stage --dry-run=client -o yaml | kubectl apply -f -