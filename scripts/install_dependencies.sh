###Updating ubuntu packages
sudo apt-add-repository "deb http://apt.kubernetes.io/ kubernetes-xenial main"
sudo apt-get update
sudo apt-get install curl
sudo apt-get install maven
sudo apt install openjdk-17-jdk openjdk-17-jre
sudo apt-get install build-essential
#install docker and kubernetes
sudo apt-get install -y docker.io
curl -s https://packages.cloud.google.com/apt/doc/apt-key.gpg | sudo apt-key add
sudo apt-get install -y kubeadm kubelet kubectl
sudo systemctl enable docker
sudo systemctl enable kubelet
sudo systemctl start docker
sudo systemctl start kubelet
#install minikube
sudo curl -LO https://storage.googleapis.com/minikube/releases/latest/minikube-linux-amd64
sudo install minikube-linux-amd64 /usr/local/bin/minikube
minikube addons enable ingress
#install skaffold
curl -Lo skaffold https://storage.googleapis.com/skaffold/releases/latest/skaffold-linux-amd64
sudo install skaffold /usr/local/bin/