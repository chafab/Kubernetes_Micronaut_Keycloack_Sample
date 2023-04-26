### Usage
To successfully run example applications you need to have:
1. JDK17+ as a default Java on your machine
2. Maven 3.5+ available under PATH
3. Minikube (I tested on version `1.29.0`)
4. Skaffold available under PATH

The script install_dependencies.sh can help you install the dependencies specified above 

### Installing the kubernetes services
If you have not done so install the dependencies. Instructions can be found in the dedicated section below.
Then proceed by doing `Minikube start`

if you have any issues with minikube when starting please use the following command:
```
sudo usermod -aG docker $USER && newgrp docker
```

Before running any application in default namespace we need to set the appropriate permissions.  
Micronaut Kubernetes requires read access to pods, endpoints, secrets, services and config maps.  
For development needs we may set the highest level of permissions by creating ClusterRoleBinding pointing to cluster-admin role.
```
kubectl create clusterrolebinding admin --clusterrole=cluster-admin --serviceaccount=default:default
```
We also need to enable ingress, as it will be used as our gateway
```
minikube addons enable ingress
```

The commands below are used to set the databases, volumes and loadbalancer that will be used to access the services  
`kubectl apply -f 01-storage-deployment.yaml` : creates a storage for the postgresdb  
`kubectl apply -f 02-postgres-deployment.yaml` : creates the postgresdb that can be used by keycloak. keycloak also support MySQL  
`kubectl apply -f 03-mongo-deployment.yaml` : creates the MongoDB that will be used in our services. Note that usually each service should have it's own DB which is not the case here.  
`kubectl apply -f 04-keycloak-deployment.yaml` : creates the keycloak service and can be accessed by forwarding the port and using the login admin password admin
`kubectl apply -f 05-haproxy-ingress.yaml` : creates the ingress component that can be used to access the different services
`kubectl apply -f 06-ingress-deployment.yaml` : creates the ingress component that can be used to access the different services
`kubectl get deployments -o yaml` : get all deployments
if there are any issues with 6th deployment file apply :
Now each of our services can be run in each directory by running

The above commands can also be executed with the `install.sh` script  