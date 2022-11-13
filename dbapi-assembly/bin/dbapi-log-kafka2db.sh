#!/bin/bash

usage="Usage: log-kafka2db.sh <group>"

# if no args specified, show usage
if [ $# -lt 1 ]; then
  echo $usage
  exit 1
fi

group=$1
shift

BIN_DIR=`dirname $0`
BIN_DIR=`cd "$BIN_DIR"; pwd`
DBAPI_HOME=`cd "$BIN_DIR/.."; pwd`

export DBAPI_CONF_DIR=$DBAPI_HOME/conf
export DBAPI_LIB_DIR=$DBAPI_HOME/lib/

CLASS=com.gitee.freakchicken.dbapi.basic.log.AccessLogKafkaReader

command="java -cp $DBAPI_LIB_DIR/*:$DBAPI_CONF_DIR $CLASS $group"
nohup $command > /dev/null 2>&1 &
