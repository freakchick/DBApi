#!/bin/sh

workDir=`dirname $0`
workDir=`cd ${workDir};pwd`
DBAPI_HOME=`cd "$workDir/.."; pwd`
source $DBAPI_HOME/conf/install_config.conf

apiServerHost=(${apiServers//,/ })
for host in ${apiServerHost[@]}
do
  echo "$host api server is starting"
	state=`ssh -p $sshPort $host "cd $DBAPI_HOME/; sh bin/dbapi-daemon.sh start apiServer;"`
	echo "$host  $state"
done

state=`ssh -p $sshPort $manager  "cd $DBAPI_HOME/; sh bin/dbapi-daemon.sh start manager;"`
echo "$manager  $state"

state=`ssh -p $sshPort $gateway  "cd $DBAPI_HOME/; sh bin/dbapi-daemon.sh start gateway;"`
echo "$gateway  $state"
