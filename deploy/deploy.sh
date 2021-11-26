#!/bin/bash

DOCKER_APP_NAME=3dollar-api
ECR_REGISTRY=332063489256.dkr.ecr.ap-northeast-2.amazonaws.com
ECR_REGION=ap-northeast-2

aws ecr get-login-password --region ${ECR_REGION} | docker login --username AWS --password-stdin ${ECR_REGISTRY}

EXIST_BLUE=$(docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml ps | grep Up)

if [ -z "$EXIST_BLUE" ]; then
    echo "[Blue] 서버를 가동합니다"
    docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml up -d --build

    sleep 120

    echo "[Green] 서버를 중지합니다"
    docker-compose -p ${DOCKER_APP_NAME}-green -f docker-compose.green.yml down
else
    echo "[Green] 서버를 가동합니다"
    docker-compose -p ${DOCKER_APP_NAME}-green -f docker-compose.green.yml up -d --build

    sleep 120

    echo "[Blue] 서버를 중지합니다"
    docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml down
fi
