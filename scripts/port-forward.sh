#!/bin/bash

# 종료 신호(Ctrl+C)를 받으면 모든 백그라운드 프로세스를 종료(while 루프 포함)
trap 'kill $(jobs -p) 2>/dev/null' EXIT

# 기존 프로세스 정리
pkill -f "kubectl port-forward" 2>/dev/null || true
sleep 1

echo "------------------------------------------------"
echo "🚀 Starting Auto-Retry Port-Forwarding"
echo "📌 Press Ctrl+C to stop all forwarding."
echo "------------------------------------------------"

# 포트 포워딩 함수
function forward_port() {
  local target=$1
  local ports=$2
  while true; do
    echo "🔗 Forwarding $target ($ports)..."
    kubectl port-forward "$target" "$ports"
    echo "⚠️ Connection to $target lost. Retrying in 2 seconds..."
    sleep 2
  done
}

# MySQL (13306:3306)
forward_port service/mysql 13306:3306 &
# Prometheus (19090:9090)
forward_port service/prometheus 19090:9090 &
# Grafana (13000:3000)
forward_port service/grafana 13000:3000 &
# Loki (13100:3100)
forward_port service/loki 13100:3100 &

echo "------------------------------g------------------"
echo "✅ Port-Forwarding is Active (Auto-Retry Enabled)"
echo "👉 MySQL:      localhost:13306"
echo "👉 Prometheus: http://localhost:19090"
echo "👉 Grafana:    http://localhost:13000"
echo "👉 Loki:       http://localhost:13100"
echo "------------------------------------------------"

# 백그라운드 작업 대기
wait
