# DBApi

## 介绍
- 快捷生成数据库的http接口服务，零代码开发，只需编写sql，就可以生成http api服务。是数据库的上层应用，方便数据库数据对外发布http服务
- 使用场景：BI报表、数据可视化大屏的后端接口快速开发；前端人员快速开发接口进行接口联调；企业数据资产对外快速发布http服务

## 特点
- 支持动态添加、修改api；支持api上线、下线管理
- 支持多数据源连接，支持动态添加、修改、删除数据库地址账户信息
- 支持多种类型数据库，包括mysql、 sqlserver、 postgreSql、 hive、 maridb 、oracle
- 支持所有增、删、改、查sql
- 资源占用极少，单tomcat容器运行，新api的发布并没有创建新的web容器
- 多节点集群扩展方便
- 部署简便，安装部署不需要连数据库，只有一个jar包启动即可

## 软件架构
- 采用B/S架构，springboot + vue.js 前后端分离开发
- 考虑到部署的简便性，最后打包是整合打到一个完整的jar包里
- 考虑到部署的简便性，使用sqlite数据库
- 新api的发布并没有创建新的tomcat容器，而是在单进程tomcat内创建逻辑上的api，不是物理上的真实的api，相比之下极大的节省了服务器资源。

## 安装教程

- 依赖java环境，需要安装jdk8+
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
- 填入sql ，就是api内部执行的逻辑，注意参数一定要以$符号开头
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
