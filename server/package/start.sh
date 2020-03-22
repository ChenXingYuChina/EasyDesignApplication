#!/usr/bin/env bash
sudo iptables -I INPUT -p tcp --dport 80 -j ACCEPT
sudo docker network create -d bridge easy-design-net
cd easyDesign/
sudo docker build -t easy-design:v1 .
cd ..
sudo docker run -d --rm --name ed -v `pwd .`/data:/var/lib/postgresql/data -p 8765:5432 --network easy-design-net postgres:11.0
sudo docker run -d --rm --name easydesign -v `pwd .`/testData:/var/local -p 80:80 --network easy-design-net easy-design:v1
sudo docker run -t -i --rm --name easydesign -v `pwd .`/testData:/var/local -p 80:80 --network easy-design-net easy-design:v1