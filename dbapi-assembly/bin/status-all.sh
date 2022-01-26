#!/bin/sh

workDir=`dirname $0`
workDir=`cd ${workDir};pwd`

source $workDir/../conf/install_config.sh

apiServerHost=(${apiServers//,/ })
for host in ${apiServerHost[@]}
do
  echo "$host api server is starting"
	state=`ssh -p $sshPort $apiServer  "cd $installPath/; sh bin/dbapi-daemon.sh start apiServer;"`
	echo "$host  $state"
done

state=`ssh -p $sshPort $manager  "cd $installPath/; sh bin/dbapi-daemon.sh start manager;"`
echo "$manager  $state"

state=`ssh -p $sshPort $gateway  "cd $installPath/; sh bin/dbapi-daemon.sh start gateway;"`
echo "$gateway  $state"
