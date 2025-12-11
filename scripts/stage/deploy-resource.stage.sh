#!/bin/bash
set -e

readonly WORK_DIR=$(cd "$(dirname "$0")/../.." && pwd)
cd "${WORK_DIR}"

kustomize build --enable-helm ./k8s/envs/stage | kubectl apply -f -
