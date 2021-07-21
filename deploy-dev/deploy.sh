#!/bin/bash

DOCKER_APP_NAME=3dollar-api

aws ecr get-login-password --region ap-northeast-2 | docker login --username AWS --password-stdin 332063489256.dkr.ecr.ap-northeast-2.amazonaws.com

EXIST_BLUE=$(docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml ps | grep Up)

if [ -z "$EXIST_BLUE" ]; then
    echo "[Blue] 서버를 가동합니다"
    docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml up -d --build

    sleep 20

    echo "[Green] 서버를 중지합니다"
    docker-compose -p ${DOCKER_APP_NAME}-green -f docker-compose.green.yml down
else
    echo "[Green] 서버를 가동합니다"
    docker-compose -p ${DOCKER_APP_NAME}-green -f docker-compose.green.yml up -d --build

    sleep 20

    echo "[Blue] 서버를 중지합니다"
    docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml down
fi
