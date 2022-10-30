# 安装教程

> 安装包[下载地址](http://www.51dbapi.com/) ，或者在[天翼云盘](https://cloud.189.cn/t/Jza2MzeEZVNv) 下载。

> 默认账户admin/admin
## 本地部署单机版

- 依赖java环境，先自行在服务器安装`jdk8+`，并配置环境变量
- 下载安装包解压到需要安装的目录
- 修改`conf/application.properties`文件中的以下配置
```properties
# api访问路径的统一根路径，example: http://192.168.xx.xx:8520/api/xxx
# api context
dbapi.api.context=api

# 如果不修改数据库地址将默认使用自带的内嵌元数据库sqlite
# 元数据库地址，可以使用mysql或者自带的sqlite
spring.datasource.dynamic.datasource.meta-db.driver-class-name=org.sqlite.JDBC
spring.datasource.dynamic.datasource.meta-db.url=jdbc:sqlite::resource:sqlite.db
spring.datasource.dynamic.datasource.meta-db.username=
spring.datasource.dynamic.datasource.meta-db.password=

# 将API访问日志写入日志数据库（推荐clickhouse）的方式，值只能是db/kafka/null
# db代表dbapi直连日志数据库，直接将API访问日志写入日志数据库
# kafka代表dbapi将API访问日志写入kafka，用户需要自行从kafka收集日志写入日志数据库
# null代表dbapi只会将API访问日志写入本地磁盘文件（logs/dbapi-access.log），用户需要自行从磁盘文件收集日志写入日志数据库
access.log.writer=null

# 日志数据库地址，推荐使用clickhouse，如果您不需要使用页面上的监控功能，可以不配置日志数据库地址
spring.datasource.dynamic.datasource.access-log-db.driver-class-name=ru.yandex.clickhouse.ClickHouseDriver
spring.datasource.dynamic.datasource.access-log-db.url=jdbc:clickhouse://127.0.0.1:8123/default
spring.datasource.dynamic.datasource.access-log-db.username=default
spring.datasource.dynamic.datasource.access-log-db.password=123456

```
> 如果配置了mysql作为元数据库，请先在mysql执行初始化脚本`sql/ddl_mysql.sql`
> 如果配置了日志数据库地址，请先在日志数据库执行初始化脚本`sql/access_log_clickhouse.sql`
- Linux一键启停
```shell
sh bin/dbapi-daemon.sh start standalone
sh bin/dbapi-daemon.sh stop standalone
```

- 如果是windows操作系统请右键点击`bin/dbapi.ps1`文件，选择使用PowerShell运行
> 注意windows系统只支持standalone模式运行，不支持集群模式

- 浏览器访问`http://192.168.xx.xx:8520`进入UI

## 本地部署集群版

- 集群部署依赖`nacos`、`mysql`、`redis`，请先自行安装`nacos`（推荐1.4.2版本）、`mysql`、`redis`
- 准备多台机器，每台安装`jdk8+`并配置java环境变量
- 选一台机器host1作为部署机，配置host1到其他每台机器的ssh免密登录
```shell
ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa
cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys
chmod 600 ~/.ssh/authorized_keys

for ip in host2 host3;     # 请将此处 host2 host3 替换为自己要部署的机器的 hostname
do
  ssh-copy-id  $ip   # 该操作执行过程中需要手动输入部署用户的密码
done
```
- 下载安装包并解压到部署机host1需要安装的目录

- 在mysql创建新的数据库，并执行初始化脚本`sql/ddl_mysql.sql`

- 修改`conf/application.properties`文件中的以下配置
```properties
#################################### please config properties below #####################################
# api访问路径的统一根路径，example: http://192.168.xx.xx:8520/api/xxx
# api context
dbapi.api.context=api

# 元数据库地址，集群版只能使用mysql
spring.datasource.dynamic.datasource.meta-db.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.dynamic.datasource.meta-db.url=jdbc:mysql://127.0.0.1:3306/dbapi?useSSL=false&characterEncoding=UTF-8&serverTimezone=GMT%2B8
spring.datasource.dynamic.datasource.meta-db.username=root
spring.datasource.dynamic.datasource.meta-db.password=root

# 将API访问日志写入日志数据库（推荐clickhouse）的方式，值只能是db/kafka/null
# db代表dbapi直连日志数据库，直接将API访问日志写入日志数据库
# kafka代表dbapi将API访问日志写入kafka，用户需要自行从kafka收集日志写入日志数据库
# null代表dbapi只会将API访问日志写入本地磁盘文件（logs/dbapi-access.log），用户需要自行从磁盘文件收集日志写入日志数据库
access.log.writer=null

# 日志数据库地址，推荐使用clickhouse，如果您不需要使用页面上的监控功能，可以不配置日志数据库地址
spring.datasource.dynamic.datasource.access-log-db.driver-class-name=ru.yandex.clickhouse.ClickHouseDriver
spring.datasource.dynamic.datasource.access-log-db.url=jdbc:clickhouse://127.0.0.1:8123/default
spring.datasource.dynamic.datasource.access-log-db.username=default
spring.datasource.dynamic.datasource.access-log-db.password=123456

############################## if cluster, please config properties below ##############################

# nacos address, needed if cluster mode
spring.cloud.nacos.server-addr=127.0.0.1:8848
spring.cloud.nacos.discovery.username=nacos
spring.cloud.nacos.discovery.password=nacos
spring.cloud.nacos.discovery.namespace=public

# redis address, needed if cluster mode
spring.redis.host=localhost
spring.redis.port=6379
spring.redis.database=0
spring.redis.password=

```
> 如果配置了日志数据库地址，请先在日志数据库执行初始化脚本`sql/access_log_clickhouse.sql`

- 修改`conf/install_config.conf`文件，配置要安装的机器节点
```shell
# 所有要安装DBApi的主机ip或hostname，用逗号分隔
ips=host1,host2,host3

sshPort=22

# 要安装gateway的主机
gateway=host1

# 要安装apiServer的主机，多个用逗号分隔
apiServers=host1,host2,host3

# 要安装manager的主机
manager=host2
```

- 拷贝host1中的安装文件到其他每台机器的**相同目录**，可使用脚本一键拷贝
```shell
sh bin/scp-host.sh
```

- 集群操作脚本
```shell
# 一键启动集群
sh bin/start-all.sh

# 一键停止集群
sh bin/stop-all.sh

# 手动启停单个服务
sh bin/dbapi-daemon.sh start gateway
sh bin/dbapi-daemon.sh start manager
sh bin/dbapi-daemon.sh start apiServer

sh bin/dbapi-daemon.sh stop gateway
sh bin/dbapi-daemon.sh stop manager
sh bin/dbapi-daemon.sh stop apiServer

```

- 浏览器访问`http://192.168.xx.xx:8523`进入UI; API通过gateway来访问`http://192.168.xx.xx:8525/api/xx`

## docker部署单机版

> Docker 容器通过环境变量进行配置，附录-环境变量列出了 `DBApi` 的可配置环境变量及其默认值

- 一键启动（使用dbapi自带的元数据库sqlite）
```shell
docker run -it -p 8520:8520 --name dbapi freakchicken/db-api:3.2.0 standalone
```

```shell
docker run -it -p 8520:8520 \
-e LOG_DB_URL="jdbc:clickhouse://127.0.0.1:8123/default" \
-e LOG_DB_USERNAME="default" \
-e LOG_DB_PASSWORD="123456" \
-e LOG_DB_DRIVER="ru.yandex.clickhouse.ClickHouseDriver" \
-e ACCESS_LOG_WRITER="db" \
--name dbapi \
freakchicken/db-api:3.2.0 standalone
```

- 使用自己的mysql作为元数据库（启动前需要在mysql执行初始化脚本）
```shell
docker run -it \
-p 8520:8520 \
-e DB_URL="jdbc:mysql://192.168.xx.xx:3306/dbapi?useSSL=false&characterEncoding=UTF-8&serverTimezone=GMT%2B8" \
-e DB_USERNAME="root" \
-e DB_PASSWORD="root" \
-e DB_DRIVER="com.mysql.cj.jdbc.Driver" \
-e ACCESS_LOG_WRITER="db" \
-e LOG_DB_URL="jdbc:clickhouse://127.0.0.1:8123/default" \
-e LOG_DB_USERNAME="default" \
-e LOG_DB_PASSWORD="123456" \
-e LOG_DB_DRIVER="ru.yandex.clickhouse.ClickHouseDriver" \
freakchicken/db-api:3.2.0 standalone
```
- 浏览器访问`http://192.168.xx.xx:8520`进入UI

## docker部署集群版
> Docker 容器通过环境变量进行配置，附录-环境变量 列出了 `DBApi` 的可配置环境变量及其默认值

- 集群部署依赖`nacos`、`mysql`、`redis`，请先自行安装`nacos`（推荐1.4.2版本）、`mysql`、`redis`
- 在mysql创建新的数据库，执行数据库初始化脚本（下载安装包解压获取`sql/ddl_mysql.sql`脚本）

- 启动UI服务manager
```shell
docker run -it \
-p 8523:8523 \
-e DB_URL="jdbc:mysql://192.168.xx.xx:3306/dbapi?useSSL=false&characterEncoding=UTF-8&serverTimezone=GMT%2B8" \
-e DB_USERNAME="root" \
-e DB_PASSWORD="root" \
-e DB_DRIVER="com.mysql.cj.jdbc.Driver" \
-e NACOS_ADDRESS="192.168.xx.xx:8848" \
-e NACOS_USERNAME="nacos" \
-e NACOS_PASSWORD="nacos" \
-e NACOS_NAMESPACE="public" \
-e REDIS_HOST="127.0.0.1" \
-e REDIS_PORT=6379 \
-e REDIS_DATABASE=0 \
-e REDIS_PASSWORD="" \
-e ACCESS_LOG_WRITER="db" \
-e LOG_DB_URL="jdbc:clickhouse://127.0.0.1:8123/default" \
-e LOG_DB_USERNAME="default" \
-e LOG_DB_PASSWORD="123456" \
-e LOG_DB_DRIVER="ru.yandex.clickhouse.ClickHouseDriver" \
freakchicken/db-api:3.2.0 manager
```

- 启动网关服务 gateway
```shell
docker run -it \
-p 8525:8525 \
-e DB_URL="jdbc:mysql://192.168.xx.xx:3306/dbapi?useSSL=false&characterEncoding=UTF-8&serverTimezone=GMT%2B8" \
-e DB_USERNAME="root" \
-e DB_PASSWORD="root" \
-e DB_DRIVER="com.mysql.cj.jdbc.Driver" \
-e NACOS_ADDRESS="192.168.xx.xx:8848" \
-e NACOS_USERNAME="nacos" \
-e NACOS_PASSWORD="nacos" \
-e NACOS_NAMESPACE="public" \
-e REDIS_HOST="127.0.0.1" \
-e REDIS_PORT=6379 \
-e REDIS_DATABASE=0 \
-e REDIS_PASSWORD="" \
-e ACCESS_LOG_WRITER="db" \
-e LOG_DB_URL="jdbc:clickhouse://127.0.0.1:8123/default" \
-e LOG_DB_USERNAME="default" \
-e LOG_DB_PASSWORD="123456" \
-e LOG_DB_DRIVER="ru.yandex.clickhouse.ClickHouseDriver" \
freakchicken/db-api:3.2.0 gateway
```

- 启动API服务apiServer（此服务可启动多个，构建api集群）
```shell
docker run -it \
-e DB_URL="jdbc:mysql://192.168.xx.xx:3306/dbapi?useSSL=false&characterEncoding=UTF-8&serverTimezone=GMT%2B8" \
-e DB_USERNAME="root" \
-e DB_PASSWORD="root" \
-e DB_DRIVER="com.mysql.cj.jdbc.Driver" \
-e NACOS_ADDRESS="192.168.xx.xx:8848" \
-e NACOS_USERNAME="nacos" \
-e NACOS_PASSWORD="nacos" \
-e NACOS_NAMESPACE="public" \
-e REDIS_HOST="127.0.0.1" \
-e REDIS_PORT=6379 \
-e REDIS_DATABASE=0 \
-e REDIS_PASSWORD="" \
-e ACCESS_LOG_WRITER="db" \
-e LOG_DB_URL="jdbc:clickhouse://127.0.0.1:8123/default" \
-e LOG_DB_USERNAME="default" \
-e LOG_DB_PASSWORD="123456" \
-e LOG_DB_DRIVER="ru.yandex.clickhouse.ClickHouseDriver" \
freakchicken/db-api:3.2.0 apiServer
```

- 浏览器访问`http://192.168.xx.xx:8523`进入UI; API通过gateway来访问`http://192.168.xx.xx:8525/api/xx`

## 附录
### 环境变量
> Docker部署的时候通过以下环境变量来传递参数

| 环境变量 | 默认值 | 说明 |
| -------- | ----- |----- |
| API_CONTEXT | api| 所有API的统一根路径 |
| DB_URL | jdbc:sqlite::resource:sqlite.db |元数据库地址 |
| DB_USERNAME |  | 元数据库账户|
| DB_PASSWORD |  | 元数据库密码|
| DB_DRIVER | org.sqlite.JDBC | 元数据库地址jdbc驱动|
| NACOS_ADDRESS | 127.0.0.1:8848 | 集群模式使用的nacos地址|
| NACOS_USERNAME | nacos | 集群模式使用的nacos账户|
| NACOS_PASSWORD | nacos |集群模式使用的nacos密码 |
| NACOS_NAMESPACE | public | 集群模式使用的nacos namespace|
| REDIS_HOST | 127.0.0.1 |集群模式使用的redis地址 |
| REDIS_PORT | 6379 |集群模式使用的redis端口 |
| REDIS_DATABASE | 0 |集群模式使用的redis数据库号 |
| REDIS_PASSWORD |  |集群模式使用的redis密码 |
| LOG_DB_URL | jdbc:clickhouse://127.0.0.1:8123/default |日志数据库地址 |
| LOG_DB_USERNAME | default | 日志数据库账户|
| LOG_DB_PASSWORD | 123456 | 日志数据库密码|
| LOG_DB_DRIVER | ru.yandex.clickhouse.ClickHouseDriver | 日志数据库地址jdbc驱动|
| ACCESS_LOG_WRITER | null | API访问日志写入数据库的方式|


### docker快速安装nacos
```
docker run --env MODE=standalone --name nacos -d -p 8848:8848 nacos/nacos-server:1.4.2
```
