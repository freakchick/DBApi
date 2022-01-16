#!/bin/bash

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

#source /etc/profile
#set -a
#source "${DBAPI_HOME}/conf/env/dbapi_env.sh"
#source "${DBAPI_HOME}/conf/config/install_config.conf"
#set +a

export HOSTNAME=`hostname`

export DBAPI_PID_DIR=$DBAPI_HOME/pid
export DBAPI_LOG_DIR=$DBAPI_HOME/logs
export DBAPI_CONF_DIR=$DBAPI_HOME/conf
export DBAPI_LIB_JARS=$DBAPI_HOME/lib/*

export STOP_TIMEOUT=5

if [ ! -d "$DBAPI_LOG_DIR" ]; then
  mkdir $DBAPI_LOG_DIR
fi

log=$DBAPI_LOG_DIR/dbapi-$command-$HOSTNAME.out
pid=$DBAPI_PID_DIR/dbapi-$command.pid

cd $DBAPI_HOME

export DBAPI_OPTS="-server -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=128m -Xss512k -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:LargePageSizeInBytes=128m -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 -XX:+PrintGCDetails -Xloggc:$DBAPI_LOG_DIR/gc.log -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=dump.hprof -XshowSettings:vm $DBAPI_OPTS"

#export DATABASE_TYPE=${DATABASE_TYPE:-"h2"}
#export SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE:-"default"}

if [ "$command" = "standalone" ]; then
  LOG_FILE="-Dlogging.config=classpath:logback-standalone.xml"
  CLASS=com.gitee.freakchicken.dbapi.DBApiStandalone
  HEAP_OPTS="-Xms1g -Xmx1g -Xmn512m"
  PROFILES="-Dspring.profiles.active=standalone"
  export DBAPI_OPTS="$HEAP_OPTS $DBAPI_OPTS"
#  export SPRING_PROFILES_ACTIVE="${SPRING_PROFILES_ACTIVE},api,${DATABASE_TYPE}"

elif [ "$command" = "manager" ]; then
  LOG_FILE="-Dlogging.config=classpath:logback-manager.xml"
  CLASS=com.gitee.freakchicken.dbapi.DBApiManager
  HEAP_OPTS="-Xms4g -Xmx4g -Xmn2g"
  PROFILES="-Dspring.profiles.active=manager"
  export DBAPI_OPTS="$HEAP_OPTS $DBAPI_OPTS"
#  export SPRING_PROFILES_ACTIVE="${SPRING_PROFILES_ACTIVE},master,${DATABASE_TYPE}"

elif [ "$command" = "apiServer" ]; then
  LOG_FILE="-Dlogging.config=classpath:logback-apiServer.xml"
  CLASS=com.gitee.freakchicken.dbapi.DBApiApiServer
  HEAP_OPTS="-Xms2g -Xmx2g -Xmn1g"
  PROFILES="-Dspring.profiles.active=apiServer"
  export DBAPI_OPTS="$HEAP_OPTS $DBAPI_OPTS"

elif [ "$command" = "gateway" ]; then
  LOG_FILE="-Dlogback.configurationFile=conf/logback-gateway.xml"
  CLASS=com.gitee.freakchicken.dbapi.DBApiGateWay
  HEAP_OPTS="-Xms1g -Xmx1g -Xmn512m"
  PROFILES="-Dspring.profiles.active=gateway"
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
      exec_command="$PROFILES $LOG_FILE $DBAPI_OPTS -classpath $DBAPI_CONF_DIR:$DBAPI_LIB_JARS $CLASS"
      $JAVA_HOME/bin/java $exec_command
    else
      [ -w "$DBAPI_PID_DIR" ] || mkdir -p "$DBAPI_PID_DIR"

      if [ -f $pid ]; then
        if kill -0 `cat $pid` > /dev/null 2>&1; then
          echo $command running as process `cat $pid`.  Stop it first.
          exit 1
        fi
      fi

      echo starting $command, logging to $log
      exec_command="$PROFILES $LOG_FILE $DBAPI_OPTS -classpath $DBAPI_CONF_DIR:$DBAPI_LIB_JARS $CLASS"
      echo "nohup $JAVA_HOME/bin/java $exec_command > $log 2>&1 &"
      nohup $JAVA_HOME/bin/java $exec_command > $log 2>&1 &
      echo $! > $pid
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