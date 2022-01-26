#!/bin/sh

workDir=`dirname $0`
workDir=`cd ${workDir};pwd`

source $workDir/../conf/install_config.sh

apiServerHost=(${apiServers//,/ })
for apiServer in ${apiServerHost[@]}
do
  echo "$apiServer api server is starting"
	ssh -p $sshPort $apiServer  "cd $installPath/; sh bin/dbapi-daemon.sh start apiServer;"
done

ssh -p $sshPort $manager  "cd $installPath/; sh bin/dbapi-daemon.sh start manager;"

ssh -p $sshPort $gateway  "cd $installPath/; sh bin/dbapi-daemon.sh start gateway;"

# query server status
echo "query server status"
cd $installPath/; sh bin/status-all.sh