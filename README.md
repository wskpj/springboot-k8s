### First Setup
```bash
./init_kind.sh
```

# Deploy K8s Resources
```bash
kubectl apply -f k8s/stage/
```

### Build & Deploy
```bash
./deploy.stage.sh
```

### Access
```bash
curl -v http://localhost
```
