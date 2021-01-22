# dbApi

## 介绍
- 快捷生成http接口服务，零代码开发，只需编写sql，就可以生成api服务。是数据库的上层应用，方便企业数据资产对外发布http服务
- 支持mysql/postgresql/hive/sqlserver/
- 目前仅支持查询类sql
- 部署简便，不需要连数据库，只有一个jar包启动即可

## 软件架构
- 采用B/S架构，springboot + vue.js 前后端分离开发
- 考虑到部署的简便性，最后打包是整合打到一个完整的jar包里
- 考虑到部署的简便性，使用sqlite数据库

## 安装教程

- 依赖java环境，需要安装jdk8+
- 下在 dbApi.jar 和数据库文件data.db, 放在同级目录
- java -jar dbApi.jar 一键启动
- 浏览器访问 http://localhost:8520/
