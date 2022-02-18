#!/bin/sh

usage="Usage: dbapi-daemon.sh (start|stop|status) <standalone|manager|apiServer|gateway> "

# if no args specified, show usage
if [ $# -le 1 ]; then
  echo $usage
  exit 1
fi

startStop=$1
shift
command=$1
shift

echo "Begin $startStop $command......"

BIN_DIR=`dirname $0`
BIN_DIR=`cd "$BIN_DIR"; pwd`
DBAPI_HOME=`cd "$BIN_DIR/.."; pwd`

export HOSTNAME=`hostname`

export DBAPI_PID_DIR=$DBAPI_HOME/pid
export DBAPI_LOG_DIR=$DBAPI_HOME/logs
export DBAPI_CONF_DIR=$DBAPI_HOME/conf
export DBAPI_LIB_DIR=$DBAPI_HOME/lib/

export STOP_TIMEOUT=5

if [ ! -d "$DBAPI_LOG_DIR" ]; then
  mkdir $DBAPI_LOG_DIR
fi

log=$DBAPI_LOG_DIR/dbapi-$command-$HOSTNAME.out
pid=$DBAPI_PID_DIR/dbapi-$command.pid

cd $DBAPI_HOME

export DBAPI_OPTS="-server -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=128m -Xss512k -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:LargePageSizeInBytes=128m -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 -XX:+PrintGCDetails -Xloggc:$DBAPI_LOG_DIR/gc.log -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=dump.hprof -XshowSettings:vm $DBAPI_OPTS"

# 判断第一个参数包含第二个参数开头的字符串
function contain() {
  local array=$1
  for item in ${array[*]}; do
    if [[ $2 =~ ^"${item}-".* ]]; then
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
  cp=$DBAPI_CONF_DIR
  for tmp in $(ls $DBAPI_LIB_DIR); do
    contain "${exclude_jars[*]}" $tmp
    res=$(echo $?)
    if [ $res = "1" ]; then
      cp=$cp:$DBAPI_LIB_DIR$tmp #不包含在其中就拼接
    fi
  done
}

if [ "$command" = "standalone" ]; then
  exclude_jars=("spring-boot-starter-webflux" "spring-webflux" "spring-cloud-gateway-server" "spring-cloud-starter-gateway")
  generate_classpath
  CLASS=com.gitee.freakchicken.dbapi.DBApiStandalone
  HEAP_OPTS="-Xms4g -Xmx4g -Xmn2g"
  PROFILES="-Dspring.profiles.active=standalone"
  export DBAPI_OPTS="$HEAP_OPTS $DBAPI_OPTS"

elif [ "$command" = "manager" ]; then
  exclude_jars=("spring-boot-starter-webflux" "spring-webflux" "spring-cloud-gateway-server" "spring-cloud-starter-gateway")
  generate_classpath
  CLASS=com.gitee.freakchicken.dbapi.manager.DBApiManager
  HEAP_OPTS="-Xms1g -Xmx1g -Xmn512m"
  PROFILES="-Dspring.profiles.active=manager"
  export DBAPI_OPTS="$HEAP_OPTS $DBAPI_OPTS"

elif [ "$command" = "apiServer" ]; then
  exclude_jars=("spring-boot-starter-webflux" "spring-webflux" "spring-cloud-gateway-server" "spring-cloud-starter-gateway")
  generate_classpath
  CLASS=com.gitee.freakchicken.dbapi.apiserver.DBApiApiServer
  HEAP_OPTS="-Xms4g -Xmx4g -Xmn2g"
  PROFILES="-Dspring.profiles.active=apiServer"
  export DBAPI_OPTS="$HEAP_OPTS $DBAPI_OPTS"

elif [ "$command" = "gateway" ]; then
  exclude_jars=("spring-boot-starter-tomcat" "spring-boot-starter-web" "tomcat-embed-websocket" "tomcat-embed-core" "spring-webmvc")
  generate_classpath
  CLASS=com.gitee.freakchicken.dbapi.gateway.DBApiGateWay
  HEAP_OPTS="-Xms4g -Xmx4g -Xmn2g"
  PROFILES="-Dspring.profiles.active=gateway -Dreactor.netty.http.server.accessLogEnabled=true "
  export DBAPI_OPTS="$HEAP_OPTS $DBAPI_OPTS"

else
  echo "Error: No command named '$command' was found."
  exit 1
fi

case $startStop in
  (start)
    if [ "$DOCKER" = "true" ]; then
      echo start $command in docker
      export DBAPI_OPTS="$DBAPI_OPTS -XX:-UseContainerSupport"
      exec_command="$PROFILES -classpath $cp $CLASS"
#      echo $exec_command
      java $exec_command
    else
      [ -w "$DBAPI_PID_DIR" ] || mkdir -p "$DBAPI_PID_DIR"

      if [ -f $pid ]; then
        if kill -0 `cat $pid` > /dev/null 2>&1; then
          echo $command running as process `cat $pid`.  Stop it first.
          exit 1
        fi
      fi

      echo starting $command
      exec_command="$PROFILES -classpath $cp $CLASS"
    #  echo "nohup java $exec_command > $log 2>&1 &"
      java $exec_command

    fi
    ;;

  (stop)

      if [ -f $pid ]; then
        TARGET_PID=`cat $pid`
        if kill -0 $TARGET_PID > /dev/null 2>&1; then
          echo stopping $command
          kill $TARGET_PID
          sleep $STOP_TIMEOUT
          if kill -0 $TARGET_PID > /dev/null 2>&1; then
            echo "$command did not stop gracefully after $STOP_TIMEOUT seconds: killing with kill -9"
            kill -9 $TARGET_PID
          fi
        else
          echo no $command to stop
        fi
        rm -f $pid
      else
        echo no $command to stop
      fi
      ;;

  (status)
    # more details about the status can be added later
    serverCount=`ps -ef |grep "$CLASS" |grep -v "grep" |wc -l`
    state="STOP"
    #  font color - red
    state="[ \033[1;31m $state \033[0m ]"
    if [[ $serverCount -gt 0 ]];then
      state="RUNNING"
      # font color - green
      state="[ \033[1;32m $state \033[0m ]"
    fi
    echo -e "$command  $state"
    ;;

  (*)
    echo $usage
    exit 1
    ;;

esac

echo "End $startStop $command."