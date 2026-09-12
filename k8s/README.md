# Kubernetes

Os manifests em `kubernetes-services.yml` criam:

- MySQL com volume persistente e Service `ClusterIP`.
- Exchange Service com Service `ClusterIP`.
- Book Service com Service `ClusterIP`.
- API Gateway com Service `LoadBalancer`.
- Probes de startup, readiness e liveness.

## Credenciais do MySQL

Crie o Secret antes de aplicar os manifests. Ele nao deve ser versionado:

```bash
kubectl create secret generic mysql-credentials \
  --from-literal=root-password='root' \
  --from-literal=username='spring' \
  --from-literal=password='spring' \
  --dry-run=client -o yaml | kubectl apply -f -
```

Os valores acima sao apenas para o ambiente local de estudo.

## Aplicacao

Se os recursos ainda nao existem ou ja foram criados por estes manifests:

```bash
kubectl apply -f k8s/kubernetes-services.yml
```

Os Deployments atuais foram criados com `kubectl create` e tiveram variaveis adicionadas por `kubectl set env`. Para migrar esses recursos imperativos para os manifests com `secretKeyRef`, recrie os Deployments uma unica vez:

```bash
kubectl delete deployment mysql exchange-service book-service api-gateway
kubectl apply -f k8s/kubernetes-services.yml
```

Essa primeira migracao recria o MySQL usando o novo volume persistente. Os dados do container MySQL imperativo, que nao possuia volume, nao sao preservados.

Acompanhe a inicializacao:

```bash
kubectl rollout status deployment/mysql --timeout=180s
kubectl rollout status deployment/exchange-service --timeout=180s
kubectl rollout status deployment/book-service --timeout=180s
kubectl rollout status deployment/api-gateway --timeout=180s
```

Verifique os recursos:

```bash
kubectl get pods,services,persistentvolumeclaims
```

O Gateway fica disponivel na porta `8765` do Load Balancer. Book, Exchange e MySQL sao acessiveis apenas dentro do cluster pelos nomes `book-service`, `exchange-service` e `mysql`.

Em ambientes que nao provisionam Load Balancers automaticamente, use:

```bash
kubectl port-forward service/api-gateway 8765:8765
```

Para deployments rastreaveis, substitua `latest` nos manifests pela tag `${{ github.sha }}` publicada pelo pipeline.
