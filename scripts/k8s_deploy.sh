cd `dirname $0` 
cd ../k8s
kubectl apply -f 01-storage-deployment.yaml
kubectl apply -f 02-postgres-deployment.yaml
kubectl apply -f 03-mongo-deployment.yaml
kubectl apply -f 04-keycloak-deployment.yaml
kubectl apply -f 05-nginx.deploy.yaml
kubectl apply -f 06-ingress-deployment.yaml
cd ../employee-service/k8s
kubectl apply -f deployment.yaml
cd ../../department-service/k8s