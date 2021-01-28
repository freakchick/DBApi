@echo off
set home=%~dp0
echo %home%

set conf_dir=%home%..\conf
set lib_dir=%home%..\lib\*

echo %conf_dir%
echo %lib_dir%

echo "java -classpath %conf_dir%;%lib_dir% com.jq.dbapi.DBApiApplication"

java -classpath %conf_dir%;%lib_dir% com.jq.dbapi.DBApiApplicationn