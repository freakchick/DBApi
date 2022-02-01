#!/bin/sh

workDir=`dirname $0`
workDir=`cd ${workDir};pwd`
DBAPI_HOME=`cd "$workDir/.."; pwd`

source $DBAPI_HOME/conf/install_config.conf

apiServerHost=(${apiServers//,/ })
for apiServer in ${apiServerHost[@]}
do
  echo "$apiServer api server is starting"
	ssh -p $sshPort $apiServer  "cd $DBAPI_HOME/; sh bin/dbapi-daemon.sh start apiServer;"
done

ssh -p $sshPort $manager  "cd $DBAPI_HOME/; sh bin/dbapi-daemon.sh start manager;"
ssh -p $sshPort $gateway  "cd $DBAPI_HOME/; sh bin/dbapi-daemon.sh start gateway;"

# query server status
echo "query server status"
cd $DBAPI_HOME/; sh bin/status-all.sh