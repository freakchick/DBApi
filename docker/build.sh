#!/bin/bash

DIR=$(dirname "$0")/..
ROOT_DIR=`cd "$DIR"; pwd`
echo $ROOT_DIR
cd $ROOT_DIR
VERSION=$(mvn -q -DforceStdout -N org.apache.maven.plugins:maven-help-plugin:3.2.0:evaluate -Dexpression=project.version)
TAG=${TAG:-"$VERSION"}
docker build --build-arg VERSION=$VERSION -t freakchicken/db-api:$TAG "$ROOT_DIR"/docker/