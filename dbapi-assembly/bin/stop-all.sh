#!/bin/sh

workDir=`dirname $0`
workDir=`cd ${workDir};pwd`

set -a
source $workDir/../conf/install_config.sh
set +a

apiServerHost=(${apiServers//,/ })
for apiServer in ${apiServerHost[@]}
do
  echo "$apiServer api server is starting"
	ssh -p $sshPort $apiServer  "cd $installPath/; sh bin/dbapi-daemon.sh stop apiServer;"
done

ssh -p $sshPort $manager  "cd $installPath/; sh bin/dbapi-daemon.sh stop manager;"

ssh -p $sshPort $gateway  "cd $installPath/; sh bin/dbapi-daemon.sh stop gateway;"

# query server status
echo "query server status"
cd $installPath/; sh bin/status-all.sh