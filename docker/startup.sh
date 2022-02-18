#!/bin/sh
# docker 容器内使用此脚本启动

# 环境变量替换成spring认识的格式
source /opt/init_config.sh

#这里用source不行
exec bin/dbapi.sh start $1
