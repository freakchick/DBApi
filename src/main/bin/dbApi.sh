#!/bin/sh

BIN_DIR=`dirname $0`
BIN_DIR=`cd "$BIN_DIR"; pwd`
HOME=$BIN_DIR/..

export CONF_DIR=$HOME/conf
export LIB_JARS=$HOME/lib/*

java -classpath $CONF_DIR:$LIB_JARS com.jq.dbapi.DBApiApplication