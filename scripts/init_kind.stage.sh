# 1. Add NGINX Ingress Controller
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/deploy/static/provider/kind/deploy.yaml

# Add nodeSelector to Ingress Controller
kubectl patch deployment ingress-nginx-controller -n ingress-nginx --patch '{"spec": {"template": {"spec": {"nodeSelector": {"node-role.kubernetes.io/control-plane": ""}}}}}'

# 2. Deploy Metrics Server
kubectl apply -f https://github.com/kubernetes-sigs/metrics-server/releases/latest/download/components.yaml

# Add TLS insecure option to Deployment
kubectl patch deployment metrics-server -n kube-system --type='json' -p='[{"op": "add", "path": "/spec/template/spec/containers/0/args/-", "value": "--kubelet-insecure-tls"}]'