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
  local array=$1
  for item in ${array[*]}; do
    if [[ $2 =~ ^"${item}-".* ]]; then
      echo $2
      return 0
    fi
  done
  return 1
}
#定义classpath变量
cp=
exclude_jars=()
# 给cp拼接jar包全路径，排除掉不需要的jar
function generate_classpath() {
  cp=$CONF_DIR
  for tmp in $(ls $LIB_DIR); do
    contain "${exclude_jars[*]}" $tmp
    res=$(echo $?)
    if [ $res = "1" ]; then
      cp=$cp:$LIB_DIR$tmp #不包含在其中就拼接
    fi
  done
}

if [ $1 = "standalone" ]; then
  exclude_jars=("spring-boot-starter-webflux" "spring-webflux" "spring-cloud-gateway-server" "spring-cloud-starter-gateway")
  generate_classpath
  java -Dspring.profiles.active=standalone -classpath $cp com.gitee.freakchicken.dbapi.DBApiStandalone

elif [ $1 = "manager" ]; then
  exclude_jars=("spring-boot-starter-webflux" "spring-webflux" "spring-cloud-gateway-server" "spring-cloud-starter-gateway")
  generate_classpath
  java -Dspring.profiles.active=manager -classpath $cp com.gitee.freakchicken.dbapi.DBApiManager

elif [ $1 = "apiServer" ]; then
  exclude_jars=("spring-boot-starter-webflux" "spring-webflux" "spring-cloud-gateway-server" "spring-cloud-starter-gateway")
  generate_classpath
  java -Dspring.profiles.active=apiServer -classpath $cp com.gitee.freakchicken.dbapi.DBApiApiServer

elif [ $1 = "gateway" ]; then
  exclude_jars=("spring-boot-starter-tomcat" "spring-boot-starter-web" "tomcat-embed-websocket" "tomcat-embed-core" "spring-webmvc")
  generate_classpath
  java -Dspring.profiles.active=gateway -classpath $cp com.gitee.freakchicken.dbapi.DBApiGateWay

else
  echo "parameter invalid"
fi
