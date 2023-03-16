# Keycloak setup

In this section we are going to install keycloak and do all the setup required so that the employee service can use it.

Deploy keycloak to kubernetes :
```
kubectl apply -f keycloak-deployment.yaml
``` 
Keycloak can then be accessed by forwarding the port and using the login admin password admin for example :
```
kubectl get services
NAME         TYPE           CLUSTER-IP       EXTERNAL-IP   PORT(S)          AGE
keycloak     LoadBalancer   10.107.213.174   <pending>     8080:30753/TCP   9d
```
Keycloak in the cluser is using port 8080 and can be accessed within the cluster on port 30753.  
To access it on a webbrowser on port 22222, run the following command
```
kubectl port-forward service/keycloak 22222:8080
```
Now you can login to keycloak using the above credentials at http://localhost:22222  
Our services need a client to be able to connect to keycloack validate that the user is valid and retrieve its credentials.

#Login
To create a client click on the administration console :
![Microservices Diagram](pictures/keycloak_login_01.jpg)

Enter the credientials admin for the login and admin for the password:
![Microservices Diagram](pictures/keycloak_login_02.jpg)

#Creating the Realm
Click on Master then Create Realm
![Microservices Diagram](pictures/keycloak_realm_01.png)

In our employee service config we use the realm nekonex_realm, set it up like below and click on Create:
![Microservices Diagram](pictures/keycloak_realm_02.png)
# Creating the client
Now click under clients
![Microservices Diagram](pictures/keycloak_client_01.png)

Click Create client
![Microservices Diagram](pictures/keycloak_client_02.png)

In our employee service config we use the client nekonex_keycloak, set it up like below and click on Next:
![Microservices Diagram](pictures/keycloak_client_03.png)

On the next screens choose :  
- Client authentication
- Standard flow
- Direct Access grants 
- OAuth 2.0 Device Authorization Grant  
then click on Next:
![Microservices Diagram](pictures/keycloak_client_04.png)

Then on the next screen click on save
![Microservices Diagram](pictures/keycloak_client_05.png)

#Credentials
 To get the credentials that our service will use to connect click on Clients and click on nekonex_client
 ![Microservices Diagram](pictures/keycloak_cred_01.png)

On the next screen click on Credentials
![Microservices Diagram](pictures/keycloak_cred_02.png)

Copy now the credential secret in this case vpZc32tNBFUbzqZpbSsBwETiT0flVTXV and put it in bootstrap.yml inside employee-service/src/main/resource/bootstrap.yml 
at client-secret: vpZc32tNBFUbzqZpbSsBwETiT0flVTXV:
![Microservices Diagram](pictures/keycloak_cred_03.png)

#Create a user
Normally the registration would be done on your html page, but one can also create a user directly on keycloak to do so :  
click on User then Create User
![Microservices Diagram](pictures/keycloak_user_01.png)

our user will be named nekonex_user put the option Email verified to yes
![Microservices Diagram](pictures/keycloak_user_02.png)

Then click on Credentials and set password
![Microservices Diagram](pictures/keycloak_user_03.png)

On the password window enter nekonex_password and set temporary to Off
![Microservices Diagram](pictures/keycloak_user_04.png)

#Congratulations you completed the Keycloak configuration for this tutorial!