kubectl create deployment web --image=gcr.io/google-samples/hello-app:1.0
kubectl expose deployment web --type=NodePort --port=8080
kubectl create deployment web2 --image=gcr.io/google-samples/hello-app:2.0
kubectl expose deployment web2 --port=8080 --type=NodePort
minikube addons enable ingress
kubectl create clusterrolebinding admin --clusterrole=cluster-admin --serviceaccount=default:default
