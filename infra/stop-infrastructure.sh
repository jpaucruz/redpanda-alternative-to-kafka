#!/bin/bash

BASEDIR=$(dirname "$0")

# build infrastructure
docker compose -f $BASEDIR/docker/docker-compose.yml down -v
