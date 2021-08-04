# DBApi

## 1.介绍

- 快速生成数据库的http接口服务，零代码开发，只需编写sql，就可以生成http api服务。是数据库的上层应用，方便数据库数据对外发布http服务
- 体验地址： http://47.92.117.90:8520/  默认账户： admin/admin
## 2.使用场景

- BI报表、数据可视化大屏的后端接口快速开发；
- 前端程序员快速开发后端接口进行接口联调；
- 企业数据资产对外快速发布http服务及统一管理

## 3.特点
- 支持动态添加、修改api；支持api上线、下线管理
- 支持API级别的访问权限控制，支持IP白名单、黑名单控制
- 支持多数据源连接，支持动态添加、修改、删除数据源
- 支持多种类型数据库，包括mysql/sqlserver/postgreSql/hive/kylin/clickhouse/oracle
- 支持动态sql，类似mybatis的动态sql
- 支持sql编辑调试，包括动态sql的调试
- 支持API结果缓存，支持缓存开启/关闭
- 支持自定义代码逻辑的数据转换，比如数据脱敏
- 部署简便，安装部署不需要使用外部数据库，一键启动即可
## 4.软件架构

- 采用B/S架构，springboot + vue.js 前后端分离开发
- 考虑到部署的简便性，使用sqlite数据库
- 使用了开源的动态sql引擎[orange](https://gitee.com/freakchicken/orange)
### 权限校验流程
  ![](https://freakchicken.gitee.io/images/dbApi/20210502/lc.png)


## 5.安装教程

- 依赖java环境，需要安装jdk8+
- 下载地址： [天翼云盘](https://cloud.189.cn/t/Jza2MzeEZVNv) ，或者在发行版页面下载

**有以下2种安装方式：**

### 5.1 tar包安装

- 下载dbApi.tar.gz包
- 解压tar包，修改conf/application.properties文件中的配置:

- 如果您想使用自己的mysql作为元数据库，请修改conf/application.properties文件中的以下配置

```properties
spring.datasource.driver-class-name=
spring.datasource.url=
spring.datasource.username=
spring.datasource.password=
```

*如果您使用了mysql作为自己的元数据库，启动前请在数据库执行初始化sql脚本，脚本在sql/目录下*

- linux操作命令

```shell
# 前台启动
sh bin/dbApi.sh start
# 后台启动
sh bin/dbApi.sh -d start
# 关闭后台启动的进程
sh bin/dbApi.sh stop

```

- windows操作命令

```shell
# 前台启动
bin/dbApi.bat
```

或者直接双击 bin/dbApi.bat 文件启动

- 启动后浏览器访问 http://ip:8520 ，默认登录账户： admin/admin

### 5.2 docker安装

```shell script
docker run -d -p 8520:8520 freakchicken/db-api
```

- 启动后浏览器访问 http://ip:8520 ，默认登录账户： admin/admin

## 6.软件截图
![](https://freakchicken.gitee.io/images/dbApi/20210502/datasource_create.png)
![](https://freakchicken.gitee.io/images/dbApi/20210803/api_edit.png)
![](https://freakchicken.gitee.io/images/dbApi/20210803/sql_run.png)
![](https://freakchicken.gitee.io/images/dbApi/20210803/api_list.png)
![](https://freakchicken.gitee.io/images/dbApi/20210502/group.png)
![](https://freakchicken.gitee.io/images/dbApi/20210502/request.png)

![](https://freakchicken.gitee.io/images/dbApi/20210502/token_add.png)
![](https://freakchicken.gitee.io/images/dbApi/20210502/token.png)
![](https://freakchicken.gitee.io/images/dbApi/20210502/token_auth.png)
![](https://freakchicken.gitee.io/images/dbApi/20210502/docs.png)
![](https://freakchicken.gitee.io/images/dbApi/20210803/ip.png)

## 7.使用教程

请阅读 [详细使用教程](./dbapi-assembly/docs/instruction.md)

## 8.插件开发
- 请阅读 [插件开发指南](./dbapi-assembly/docs/plugin%20development.md)
- 作者已经开发了字段加密插件和redis缓存插件，请阅读[案例demo](https://gitee.com/freakchicken/dbapi-plugin-demo)
## 9.开发指南

### 9.1环境依赖

- 安装jdk8+
- 安装node.js
- (可选)npm设置国内源
```shell
npm config set registry https://registry.npm.taobao.org
```
- 拉取[orange](https://gitee.com/freakchicken/orange) 代码到本地并maven install安装。（作者试了多次发布orange到maven中央仓库一直没有成功，有懂的同学指导作者一下）

### 9.2编译打包

- maven打包会自动把前端安装依赖并编译打包，

```shell script
mvn clean package
```

### 9.3构建镜像
- 在dbapi-assembly目录下
```shell script
mvn docker:build
mvn docker:push
```

### 9.4启动

- 前端启动：
dbapi-ui 目录下 **npm run serve**

- 后端启动:
dbapi-core目录下启动主类com.gitee.freakchicken.dbapi.DBApiApplication

- 前端访问地址：
```
http://localhost:8521
```

- 后端接口访问地址：
```
http://localhost:8520
```
## 10.springboot集成

如果您想更加灵活的使用dbApi，在您自己的java springboot项目中使用代码配置接口，
请使用[dbApi-spring-boot-starter开源框架](https://gitee.com/freakchicken/dbApi-spring-boot-starter)

## 11.注意事项

- 如果您要使用Oracle，请将oracle jdbc驱动包手动放入解压后的lib目录下

## 12.联系作者：

### 微信：
- 提问请先star支持一下
<div style="text-align: center"> 
<img src="https://freakchicken.gitee.io/images/kafkaui/wechat.jpg" width = "30%" />
</div>

### 微信交流群：

<div style="text-align: center"> 
<img src="https://freakchicken.gitee.io/images/dbApi/wechatGroup.png" width = "40%" />
</div>

### qq交流群：

<div style="text-align: center"> 
<img src="https://freakchicken.gitee.io/images/dbApi/qqgroup.jpg" width = "40%" />
</div>

### 捐赠：

如果此项目帮助到您，请捐赠作者一杯咖啡
<div style="text-align: center"> 
<img src="https://freakchicken.gitee.io/images/kafkaui/wechatpay.jpg" width = "30%" />
<img src="https://freakchicken.gitee.io/images/kafkaui/alipay.jpg" width = "29%" />
</div>

## 13.TODO

- 集群版本开发，支持微服务注册consul/eureka/nacos
- api熔断支持
- 请求路由参数支持
- 流量监控支持，ip限流，分组限流
- api配置导入导出支持
- 功能性api支持（RPC调用）
- 分库分表数据源支持（shardingSphere/mycat）
