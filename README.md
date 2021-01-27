# DBApi

## 介绍
- 快速生成数据库的http接口服务，零代码开发，只需编写sql，就可以生成http api服务。是数据库的上层应用，方便数据库数据对外发布http服务
## 使用场景
- BI报表、数据可视化大屏的后端接口快速开发；
- 前端程序员快速开发后端接口进行接口联调；
- 企业数据资产对外快速发布http服务及统一管理

## 特点
- 支持动态添加、修改api；支持api上线、下线管理
- 支持多数据源连接，支持动态添加、修改、删除数据库地址
- 支持多种类型数据库，包括mysql、 sqlserver、 postgreSql、 hive、 maridb
- 支持接口传参，可以传任意多个参数，参数名根据sql自动解析生成（前提是sql编写遵循此软件的要求规范）
- 接口地址支持任意多级，比如 http://ip:port/api/aa/bb/cc
- 支持所有增、删、改、查sql
- 部署简便，安装部署不需要连数据库，只有一个jar包启动即可
- 资源占用极少，单tomcat容器运行，新api的发布并没有创建新的web容器
- 集群扩展方便，api非硬编码方式，而是逻辑上的api，多节点集群扩展极方便

## 软件架构
- 采用B/S架构，springboot + vue.js 前后端分离开发
- 考虑到部署的简便性，最后打包是整合打到一个完整的jar包里
- 考虑到部署的简便性，使用sqlite数据库
- 新api的发布并没有创建新的tomcat容器，而是在单进程tomcat内创建逻辑上的api，不是物理上的硬编码的api，相比之下极大的节省了服务器资源。

## 安装教程

- 依赖java环境，需要安装jdk8+
- 下载地址： https://gitee.com/freakchicken/db-api/releases
- 下载 dbApi.jar 和数据库文件data.db， 放在同级目录下
- java -jar dbApi.jar 一键启动
- 浏览器访问 http://localhost:8520/

## 使用说明

### 创建数据源
![](https://freakchicken.gitee.io/images/dbApi/add_source_20210125160727.jpg)
### 创建api
![](https://freakchicken.gitee.io/images/dbApi/add_api_20210125161115.jpg)
- 填入路径，这就是将来http请求的路径
- 选择数据源，就是接口执行sql逻辑的数据库地址
- 填入sql ，就是api内部执行的逻辑，**注意参数一定要以$符号开头，且sql类不能有两个相同的参数名**
- 点击解析参数，自动解析出sql中的参数，这个参数就是将来的http请求参数，
http请求传来的参数值会被替换进sql。同时选择参数类型，默认string
- 点击保存，返回api列表页面，可以看到新增一条记录

![](https://freakchicken.gitee.io/images/dbApi/api_list_20210125161420.jpg)
### api上线
- 点击api上线按钮，这个api就上线，可以访问了
![](https://freakchicken.gitee.io/images/dbApi/online_click_20210125161514.jpg)

### api详情
- 点击查看详情按钮，这时候可以查看api的详情
![](https://freakchicken.gitee.io/images/dbApi/api_detail_20210125161633.jpg)
### api访问测试
- 点击请求测试按钮，我们可以发起api请求，查看返回结果，这个结果就是我们之前填入的sql执行的结果
![](https://freakchicken.gitee.io/images/dbApi/request_20210125161613.jpg)

## 开发指南
### 环境依赖

- 安装jdk8+
- 安装node.js
- 安装cnpm (maven 会调用cnpm 系统命令)

```
npm install -g cnpm --registry=https://registry.npm.taobao.org
```

### 编译打包

- maven打包会自动把前端安装依赖并编译打包，

```
mvn clean package
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

## 联系作者：
### wechat：
<div style="text-align: center"> 
<img src="https://freakchicken.gitee.io/images/kafkaui/wechat.jpg" width = "30%" />
</div>


### 捐赠：
如果您喜欢此项目，请给捐助作者一杯咖啡
<div style="text-align: center"> 
<img src="https://freakchicken.gitee.io/images/kafkaui/wechatpay.jpg" width = "30%" />
</div>

## sql编写规范
- sql只支持单条sql

比如以下sql就是错误的:
```
select * from user where name =  $userName;
select * from departments where id =  $departmentId;
```
-  sql中传入的字符串、日期类型参数不需要单引号包裹

比如以下sql就是错误的:
```
select * from user where name =  '$userName'
```
正确的方式是:
```
select * from user where name =  $userName
```

-  不能有两个以上相同的参数名

比如以下sql就是错误的:
```
select id,name from table1 where id = $id
union
select id,name from table2 where id = $id
```
正确的方式是:
```
select id,name from table1 where id = $id1
union
select id,name from table2 where id = $id2
```