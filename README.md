# DBApi

## 介绍

- 快速生成数据库的http接口服务，零代码开发，只需编写sql，就可以生成http api服务。是数据库的上层应用，方便数据库数据对外发布http服务
- 体验地址： http://47.92.117.90:8520/
## 使用场景

- BI报表、数据可视化大屏的后端接口快速开发；
- 前端程序员快速开发后端接口进行接口联调；
- 企业数据资产对外快速发布http服务及统一管理

## 特点

- 支持动态添加、修改api；支持api上线、下线管理
- API支持访问权限控制，API支持分组管理
- 支持多数据源连接，支持动态添加、修改、删除数据库地址
- 支持多种类型数据库，包括mysql/sqlserver/postgreSql/hive/kylin/clickhouse/oracle
- 支持动态sql，类似mybatis的动态sql
- 部署简便，安装部署不需要使用外部数据库，一键启动即可（自带sqlite数据库作为元数据库，同时支持用户自定义使用mysql作为元数据库）

## 软件架构

- 采用B/S架构，springboot + vue.js 前后端分离开发
- 考虑到部署的简便性，最后打包是整合打到一个完整的jar包里
- 考虑到部署的简便性，使用sqlite数据库
- 使用了开源的动态sql引擎[orange](https://gitee.com/freakchicken/orange)

## springboot集成

如果您想更加灵活的使用dbApi，在您自己的java springboot项目中使用代码配置接口，
请使用[dbApi-spring-boot-starter开源框架](https://gitee.com/freakchicken/dbApi-spring-boot-starter)

## 安装教程

- 依赖java环境，需要安装jdk8+
- 下载地址： [天翼云盘](https://cloud.189.cn/t/Jza2MzeEZVNv) ，或者在发行版页面下载

**有以下3种安装方式：**

### 1.jar包安装

- 如果您想要快速安装，请下载dbApi.jar包
- 启动命令：java -jar dbApi.jar 一键启动

- 启动后浏览器访问 http://ip:8520 ，默认登录账户： admin/admin

### 2.tar包安装

- 如果您想自定义配置，请下载dbApi.tar.gz包
- 解压tar包，修改conf/application.properties文件中的端口配置:

```properties
server.port=8520
```

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

### 3.docker安装

```shell script
docker run -d -p 8520:8520 freakchicken/db-api
```

- 启动后浏览器访问 http://ip:8520 ，默认登录账户： admin/admin

## 使用说明

### 创建数据源

![](https://freakchicken.gitee.io/images/dbApi/20210502/datasource_create.png)

### 创建/修改api

![](https://freakchicken.gitee.io/images/dbApi/20210502/api_add.png)
![](https://freakchicken.gitee.io/images/dbApi/20210502/api_edit.png)

- 填入路径，这就是将来http请求的路径
- 选择数据源，就是接口执行sql逻辑的数据库地址
- 填入sql ,类似mybatis的动态sql语法，不需要写最外层的select update 标签，参数名用 #{} ${}
  表示，可以参考[这里](https://mybatis.org/mybatis-3/zh/dynamic-sql.html)
- 添加参数，参数名称就是sql中的参数名，sql中涉及到的每个参数都要填写
- API分组，选择api所属的分组，这个分组可以将来授权使用 
- 访问权限，开放接口可以直接访问，私有接口需要申请tokrn才能访问  
- 点击保存，返回api列表页面，可以看到新增一条记录

![](https://freakchicken.gitee.io/images/dbApi/20210502/api.png)

### api分组管理
- 可以添加、删除分组
![](https://freakchicken.gitee.io/images/dbApi/20210502/group.png)

### api请求测试
- 在页面快速访问API，查看结果
![](https://freakchicken.gitee.io/images/dbApi/20210502/request.png)

### 创建token
![](https://freakchicken.gitee.io/images/dbApi/20210502/token_add.png)
![](https://freakchicken.gitee.io/images/dbApi/20210502/token.png)

### 授权token可以访问哪些api
![](https://freakchicken.gitee.io/images/dbApi/20210502/token_auth.png)

### 导出接口文档
- 可以导出接口文档（markdown格式）
![](https://freakchicken.gitee.io/images/dbApi/20210502/docs.png)



## sql编写规范

和mybatis动态sql语法一样，同样支持参数#{}、 ${}，可以参考[这里](https://mybatis.org/mybatis-3/zh/dynamic-sql.html)
，不需要写最外层的select update 标签，直接写sql内容

## 开发指南

### 环境依赖

- 安装jdk8+
- 安装node.js
- 安装cnpm (maven 会调用cnpm 系统命令)

```shell script
npm install -g cnpm --registry=https://registry.npm.taobao.org
```

### 编译打包

- maven打包会自动把前端安装依赖并编译打包，

```shell script
mvn clean package -P singleJar
mvn clean package -P tar
```

### 构建镜像
```shell script
mvn docker:build -P tar
mvn docker:push -P tar
```

### 启动

#### 前端启动：

- src/main/webapp 目录下 **npm run serve**

#### 后端启动

- 启动主类com.jq.dbapi.DBApiApplication

### 前端访问地址：

```
http://localhost:8521
```

### 后端接口访问地址：

```
http://localhost:8520
```

## 注意事项

- 如果您要使用Oracle，请使用tar包安装方式，并将oracle jdbc驱动包手动放入解压后的lib目录下

## 联系作者：

### 微信：
- 提问请先star支持一下
<div style="text-align: center"> 
<img src="https://freakchicken.gitee.io/images/kafkaui/wechat.jpg" width = "30%" />
</div>

### qq交流群：

<div style="text-align: center"> 
<img src="https://freakchicken.gitee.io/images/dbApi/qqgroup.jpg" width = "40%" />
</div>

### 捐赠：

如果您喜欢此项目，请给捐助作者一杯咖啡
<div style="text-align: center"> 
<img src="https://freakchicken.gitee.io/images/kafkaui/wechatpay.jpg" width = "30%" />
<img src="https://freakchicken.gitee.io/images/kafkaui/alipay.jpg" width = "29%" />
</div>

## TODO

- 集群版本开发，支持微服务注册consul/eureka/nacos
