#!/bin/sh

BIN_DIR=`dirname $0`
BIN_DIR=`cd "$BIN_DIR"; pwd`
DBAPI_HOME=`cd "$BIN_DIR/.."; pwd`

cd $DBAPI_HOME
parentDir=$(dirname $(pwd))

source $DBAPI_HOME/conf/install_config.conf

hosts=(${ips//,/ })
for host in ${hosts[@]}
do
  echo "copy $DBAPI_HOME to $host:$parentDir ..."
  ssh -p $sshPort $host "mkdir -p $parentDir "
	scp -q -P $sshPort -r $DBAPI_HOME $host:$parentDir
done

echo "success"
