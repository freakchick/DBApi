#!/bin/bash

echo "execute startup.sh"
echo `pwd`
# 环境变量替换成spring认识的格式
source /opt/init_config.sh

source bin/dbapi.sh start $1