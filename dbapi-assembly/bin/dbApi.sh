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
export LIB_DIR=$HOME/lib/
export LOG_DIR=$HOME/logs


standalone_cp=$CONF_DIR
# shellcheck disable=SC2045
for tmp in $(ls $LIB_DIR)
do
  if [[ $tmp != dbapi-cluster-* ]]; then
    standalone_cp=$standalone_cp:$LIB_DIR$tmp
  fi
done

manager_cp=$CONF_DIR
# shellcheck disable=SC2045
for tmp in $(ls $LIB_DIR)
do
  if [[ $tmp != dbapi-standalone-* ]] && [[ $tmp != dbapi-cluster-apiServer-* ]] ; then
    manager_cp=$manager_cp:$LIB_DIR$tmp
  fi
done

api_cp=$CONF_DIR
# shellcheck disable=SC2045
for tmp in $(ls $LIB_DIR)
do
  if [[ $tmp != dbapi-standalone-* ]] && [[ $tmp != dbapi-cluster-manager-* ]] ; then
    api_cp=$api_cp:$LIB_DIR$tmp
  fi
done

gateway_cp=$CONF_DIR/application.yml
# shellcheck disable=SC2045
for tmp in $(ls $LIB_DIR)
do
  if [[ $tmp != dbapi-* ]] ; then
    gateway_cp=$gateway_cp:$LIB_DIR$tmp
  fi

  if [[ $tmp == dbapi-cluster-gateway-* ]] ; then
    gateway_cp=$gateway_cp:$LIB_DIR$tmp
  fi
done

if [ $1 = "standalone" ]; then
  echo $standalone_cp
  java -Dspring.profiles.active=standalone -classpath $standalone_cp com.gitee.freakchicken.dbapi.DBApiStandalone
#  if [ "$bool" = "false" ]; then
#    java -Dlogging.file=$LOG_DIR/dbApi.log -classpath $CONF_DIR:$LIB_DIR com.gitee.freakchicken.dbapi.DBApiStandalone
#  else
#    nohup java -Dlogging.file=$LOG_DIR/dbApi.log -classpath $CONF_DIR:$LIB_DIR com.gitee.freakchicken.dbapi.DBApiStandalone >/dev/null 2>&1 &
#    echo $! >$PID
#  fi
elif [ $1 = "manager" ]; then
  echo $manager_cp
  java -Dspring.profiles.active=manager -classpath $manager_cp com.gitee.freakchicken.dbapi.DBApiManager
elif [ $1 = "apiServer" ]; then
  echo $api_cp
  java -Dspring.profiles.active=apiServer -classpath $api_cp com.gitee.freakchicken.dbapi.DBApiApiServer
elif [ $1 = "gateway" ]; then
  echo $gateway_cp
  java -classpath $gateway_cp com.gitee.freakchicken.dbapi.DBApiGateWay

elif [ $1 = "stop" ]; then
  TARGET_PID=$(cat $PID)
  kill $TARGET_PID

else
  echo "parameter invalid"
fi
