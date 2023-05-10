#!/bin/bash

BASEDIR=$(dirname "$0")
PWD=$(pwd)
MOVEMENTS_PATH=$PWD/$BASEDIR/apps/movements
STREAM_PATH=$PWD/$BASEDIR/apps/stream
DOCKER_PATH=$PWD/$BASEDIR/docker

# build apps
echo '---------- build docker images ----------'
echo $PWD

cd $MOVEMENTS_PATH
docker build --no-cache -t com.paradigmadigital/redpanda-movements-app:0.1 .

cd $STREAM_PATH
docker build --no-cache -t com.paradigmadigital/redpanda-stream-app:0.1 .

sleep 5

# deploy infrastructure
echo '---------- deploy infra ----------'
cd $DOCKER_PATH
docker compose up -d
docker exec -it redpanda-0 rpk topic create card-movements
docker exec -it redpanda-0 rpk topic create card-fraud
