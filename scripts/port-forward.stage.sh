#!/bin/bash

# 종료 신호(Ctrl+C)를 받으면 백그라운드 프로세스들도 모두 종료하도록 설정
trap 'kill $(jobs -p) 2>/dev/null' EXIT

# 기존에 혹시 남아있을 수 있는 포트포워딩 프로세스 정리
pkill -f "kubectl port-forward" 2>/dev/null || true
sleep 1

echo "------------------------------------------------"
echo "🚀 Starting Port-Forwarding (Foreground Mode)"
echo "⏱️ Waiting a few seconds for pods to be ready..."
sleep 2
echo "📌 Press Ctrl+C to stop all forwarding."
echo "------------------------------------------------"

# MySQL (13306:3306)
kubectl port-forward service/mysql-stage 13306:3306 &

# Prometheus (19090:9090)
kubectl port-forward service/prometheus-stage 19090:9090 &

# Grafana (13000:3000)
kubectl port-forward service/grafana-stage 13000:3000 &

echo "✅ Port-Forwarding is Active:"
echo "👉 MySQL:      localhost:13306"
echo "👉 Prometheus: http://localhost:19090"
echo "👉 Grafana:    http://localhost:13000"
echo "------------------------------------------------"

# 백그라운드 작업이 끝날 때까지 대기 (Ctrl+C를 누를 때까지 포워딩 유지)
wait
