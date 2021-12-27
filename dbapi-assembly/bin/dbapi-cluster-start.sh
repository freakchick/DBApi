#!/bin/sh

bool=false

while getopts d opt; do
  case $opt in
  d)
    echo "running in daemon"
    bool=true
    shift
    ;;
  ?)
    echo "$opt is an invalid option"
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

# 判断第一个参数包含第二个参数开头的字符串
function contain() {
  array=$1
  for item in ${array[*]}; do
    if [[ $2 =~ ^"${item}-".* ]]; then
      echo $2
      return 0
    fi
  done
  return 1
}


exclude_jars=("dbapi-cluster" )
standalone_cp=$CONF_DIR
for tmp in $(ls $LIB_DIR); do
  contain "${exclude_jars[*]}" $tmp
  res=$(echo $?)
  if [ $res = "1" ]; then
    standalone_cp=$standalone_cp:$LIB_DIR$tmp #不包含在其中就拼接
  fi
done


exclude_jars=("dbapi-standalone" "dbapi-cluster-apiServer" )
manager_cp=$CONF_DIR
for tmp in $(ls $LIB_DIR); do
  contain "${exclude_jars[*]}" $tmp
  res=$(echo $?)
  if [ $res = "1" ]; then
    manager_cp=$manager_cp:$LIB_DIR$tmp #不包含在其中就拼接
  fi
done


exclude_jars=("dbapi-standalone" "dbapi-cluster-manager" )
api_cp=$CONF_DIR
for tmp in $(ls $LIB_DIR); do
  contain "${exclude_jars[*]}" $tmp
  res=$(echo $?)
  if [ $res = "1" ]; then
    api_cp=$api_cp:$LIB_DIR$tmp #不包含在其中就拼接
  fi
done


exclude_jars=("spring-boot-starter-tomcat" "spring-boot-starter-web" "tomcat-embed-websocket" "tomcat-embed-core" "spring-webmvc" "dbapi-cluster-apiServer" "dbapi-cluster-manager" "dbapi-controller" "dbapi-standalone")
gateway_cp=$CONF_DIR
for tmp in $(ls $LIB_DIR); do
  contain "${exclude_jars[*]}" $tmp
  res=$(echo $?)
  if [ $res = "1" ]; then
    gateway_cp=$gateway_cp:$LIB_DIR$tmp #不包含在其中就拼接
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
  java -Dspring.profiles.active=gateway -classpath $gateway_cp com.gitee.freakchicken.dbapi.DBApiGateWay

#elif [ $1 = "stop" ]; then
#  TARGET_PID=$(cat $PID)
#  kill $TARGET_PID
else
  echo "parameter invalid"
fi
