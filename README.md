# dbApi

## 介绍
- 快捷生成http接口服务，零代码开发，只需编写sql，就可以生成api服务。是数据库的上层应用，方便企业数据资产对外发布http服务
- 支持mysql/postgresql/hive/sqlserver/
- 部署简便，不需要连数据库，只有一个jar包启动即可
- 新api的发布并没有创建新的tomcat容器，而是在单进程tomcat内创建逻辑上的api，不是物理上的真实的api，相比之下极大的节省了服务器资源。
- 针对高并发场景，因为一个单进程内拥有所有api服务的详细参数信息，只需要修改ip端口启动新的进程，再在外层架设网关，后期就可以很方便的扩展集群。
- 目前仅支持查询类sql

## 软件架构
- 采用B/S架构，springboot + vue.js 前后端分离开发
- 考虑到部署的简便性，最后打包是整合打到一个完整的jar包里
- 考虑到部署的简便性，使用sqlite数据库

## 安装教程

- 依赖java环境，需要安装jdk8+
- 下载 dbApi.jar 和数据库文件data.db, 放在同级目录下
- java -jar dbApi.jar 一键启动
- 浏览器访问 http://localhost:8520/
