# Kubernetes Concepts
1. **Pod**  
A Pod is the smallest and simplest unit in the Kubernetes object model. It represents a single instance of a running process in a cluster and can contain one or more containers, such as Docker containers. Containers within a pod share the same network namespace, which means they can communicate with each other using localhost.

2. **Service**  
A Service is an abstraction that defines a logical set of pods and a policy to access them. It acts as a stable IP address and port that can be used to access the pods it targets. Services allow you to decouple the frontend and backend systems, making it easier to scale and update them independently.

3. **Endpoint**  
An Endpoint is a Kubernetes object that represents a network address (IP and port) where a service can be accessed. Endpoints are automatically created by the Kubernetes control plane when a service selects one or more pods through label selectors.
 
4. **Ingress**  
Ingress is a Kubernetes object that provides external access to services in a cluster, typically via HTTP or HTTPS. It allows you to define rules for routing external traffic to the appropriate services based on the requested host and path. Ingress uses a reverse proxy, like NGINX or HAProxy, to manage the traffic.

## Kubernetes Cheat Sheet
List all ingresses across all namespaces:  
`kubectl get ingress --all-namespaces`
for example ::
```
NAMESPACE   NAME              CLASS    HOSTS                  ADDRESS        PORTS   AGE
default     nekonex-ingress   <none>   nekonex-ingress.info   192.168.49.2   80      11d
```

Delete a specific ingress:  
`kubectl delete ingress my-example-ingress`

Apply an ingress-nginx deployment from a URL:  
`kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.1.0/deploy/static/provider/cloud/deploy.yaml`

List services:  
`kubectl get services`

for example :
```
NAME           TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)          AGE
department     NodePort    10.101.202.105   <none>        8080:30266/TCP   88s
employee       NodePort    10.111.127.76    <none>        8080:30377/TCP   22m
kubernetes     ClusterIP   10.96.0.1        <none>        443/TCP          12h
mongodb        ClusterIP   10.106.41.104    <none>        27017/TCP        12h
```

Describe a specific ingress:  
`kubectl describe ingress <ingress_name>`


Get the URL for the ingress-nginx controller service in the Minikube environment:  
`minikube service -n ingress-nginx ingress-nginx-controller --url`

Get the URL for the HAProxy controller service in the Minikube environment:  
`minikube service -n haproxy-controller haproxy-kubernetes-ingress --url`

List all pods across all namespaces:  
`kubectl get pods --all-namespaces`  
for example 
```
NAMESPACE            NAME                                          READY   STATUS      RESTARTS       AGE
default              department-68d7b85944-kjztz                   1/1     Running     0              61m
default              employee-c6d46857b-xtqb4                      1/1     Running     0              65m
default              keycloak-5c8678c949-97lpc                     1/1     Running     6 (37h ago)    11d
default              mongodb-595c9db6bc-z6gmt                      1/1     Running     0              14h
default              postgres-77dc9d4f6d-vjhl8                     1/1     Running     5 (37h ago)    11d
default              web-68487bc957-bqdj6                          1/1     Running     5 (37h ago)    11d
default              web2-6459878f46-xcm8t                         1/1     Running     5 (37h ago)    11d
haproxy-controller   haproxy-kubernetes-ingress-54fd7dddb8-ms64m   1/1     Running     4 (37h ago)    9d
ingress-nginx        ingress-nginx-admission-create-4mxq4          0/1     Completed   0              9d
ingress-nginx        ingress-nginx-admission-patch-n7rzv           0/1     Completed   1              9d
ingress-nginx        ingress-nginx-controller-77669ff58-gx5s5      1/1     Running     4 (37h ago)    9d
kube-system          coredns-787d4945fb-trr2b                      1/1     Running     5 (37h ago)    11d
kube-system          etcd-minikube                                 1/1     Running     5 (37h ago)    11d
kube-system          kube-apiserver-minikube                       1/1     Running     6 (37h ago)    11d
kube-system          kube-controller-manager-minikube              1/1     Running     5 (37h ago)    11d
kube-system          kube-proxy-rkt82                              1/1     Running     5 (37h ago)    11d
kube-system          kube-scheduler-minikube                       1/1     Running     5 (37h ago)    11d
kube-system          storage-provisioner                           1/1     Running     10 (49m ago)   11d
```
Execute a shell command in a specific container within a pod:  
`kubectl exec -it <pod_name> -n <namespace> -- /bin/sh`

List all endpoints:  
`kubectl get endpoints`  
for example :  
```
NAME           ENDPOINTS           AGE
department     10.244.0.8:8080     4m10s
employee       10.244.0.7:8080     25m
kubernetes     192.168.49.2:8443   12h
mongodb        10.244.0.5:27017    12h
```

If you are using wsl you can forward the services port using
```
kubectl port-forward service/department 30266:8080
```
