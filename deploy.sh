#!/bin/bash

mvn clean package -P tar
echo "package success !!"
mvn docker:build -P tar
echo "build image success !!"