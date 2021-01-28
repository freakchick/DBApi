#!/bin/sh

pid=server.pid

if [ $1 = "start" ]; then
  BIN_DIR=$(dirname $0)
  BIN_DIR=$(
    cd "$BIN_DIR"
    pwd
  )
  HOME=$BIN_DIR/..

  export CONF_DIR=$HOME/conf
  export LIB_JARS=$HOME/lib/*

  nohup java -classpath $CONF_DIR:$LIB_JARS com.jq.dbapi.DBApiApplication > /dev/null 2>&1 &
  echo $! >$pid

elif [ $1 = "stop" ]; then
  TARGET_PID=$(cat $pid)
  kill $TARGET_PID

else
  echo "parameter invalid"
fi
