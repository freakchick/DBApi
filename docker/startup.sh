#!/bin/bash

echo "execute startup.sh"
echo `pwd`
# 环境变量替换成spring认识的格式
source /opt/init_config.sh
echo `pwd`
echo "bin/dbapi.sh start $1"
source bin/dbapi.sh start $1
