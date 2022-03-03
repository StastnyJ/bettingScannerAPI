#!/bin/sh

apt-get update;
apt-get install ca-certificates curl gnupg lsb-release;
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg;
echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null;
apt-get update;
apt-get install docker-ce docker-ce-cli containerd.io;

docker pull stastnyj/betting-scanner-api;
docker stop WEB_SCANNER;
docker system prune -f;
docker run -d --name=WEB_SCANNER --restart=always -p 8080:8080 stastnyj/betting-scanner-api;
