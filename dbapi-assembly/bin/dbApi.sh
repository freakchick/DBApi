#!/bin/sh

bool=false

while getopts d opt;
do
case $opt in
  d) echo "running in daemon"
    bool=true
    shift
    ;;
  ?) echo "$opt is an invalid option"
    ;;
esac
done

BIN_DIR=$(dirname $0)
BIN_DIR=$(
  cd "$BIN_DIR"
  pwd
)
# 安装包部署的目录
HOME=$BIN_DIR/..
PID=$BIN_DIR/server.pid

export CONF_DIR=$HOME/conf
export LIB_JARS=$HOME/lib/
export LOG_DIR=$HOME/logs
ls $LIB_JARS

#for dir in $(ls $LIB_JARS)
#do
#  echo $dir
#done
#
#if [ $1 = "start" ]; then
#
#  if [ "$bool" = "false" ]; then
#    java -Dlogging.file=$LOG_DIR/dbApi.log -classpath $CONF_DIR:$LIB_JARS com.gitee.freakchicken.dbapi.DBApiStandalone
#  else
#    nohup java -Dlogging.file=$LOG_DIR/dbApi.log -classpath $CONF_DIR:$LIB_JARS com.gitee.freakchicken.dbapi.DBApiStandalone >/dev/null 2>&1 &
#    echo $! >$PID
#  fi
#
#elif [ $1 = "stop" ]; then
#  TARGET_PID=$(cat $PID)
#  kill $TARGET_PID
#
#else
#  echo "parameter invalid"
#fi
