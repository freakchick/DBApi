# DBApi

![](https://gitee.com/freakchicken/db-api/badge/star.svg)
![](https://gitee.com/freakchicken/db-api/badge/fork.svg?theme=gvp)
![](https://img.shields.io/github/stars/freakchick/DBApi.svg?logo=GitHub)
![](https://img.shields.io/github/forks/freakchick/DBApi.svg?logo=GitHub)
![](https://img.shields.io/github/watchers/freakchick/DBApi.svg?logo=GitHub)
![](https://img.shields.io/github/license/freakchick/DBApi.svg)

[![EN doc](https://img.shields.io/badge/document-English-blue.svg)](README.md)
[![CN doc](https://img.shields.io/badge/文档-中文版-blue.svg)](README_zh_CN.md)

## 介绍

- 快速生成数据库的http接口服务，零代码开发，只需编写sql，就可以生成http api服务。是数据库的上层应用，方便数据库数据对外发布http服务
- 体验地址： `http://101.34.234.234:8520/` 。  默认账户： admin/admin (**请不要修改密码**)
## 使用场景

- BI报表、数据可视化大屏的后端接口快速开发；
- 前端程序员快速开发后端接口进行接口联调；
- 企业数据资产对外快速发布http服务及统一管理
- 企业数据接口的统一管理中心

## 特点
- 开箱即用，不需要编程，不需要依赖其他软件（只需要java运行环境）
- 支持单机模式、集群模式；支持云原生容器化部署
- 支持动态创建、修改API；动态创建、修改数据源。热部署全程无感。
- 支持API级别的访问权限控制，支持IP白名单、黑名单控制
- 支持所有类型数据库（JDBC协议），包括mysql/sqlserver/postgreSql/hive/oracle等等
- 支持动态sql，类似mybatis的动态sql，支持sql编辑、运行、调试
- 丰富的插件扩展，支持缓存、数据转换、失败告警
- 支持API配置导入导出，方便测试环境到生产环境的API迁移
- 支持一个接口内多条SQL执行（例如分页功能），支持事务开启关闭
- 支持`application/json`和`application/x-www-form-urlencoded`

## 视频教程
[查看视频教程](https://www.bilibili.com/video/BV1zL411G7Qh)

## 安装教程

- 请阅读 [《安装教程》](./dbapi-assembly/docs/deployment.md)

## 软件截图
![](https://freakchicken.gitee.io/images/dbApi/20220503/api_list.png)
![](https://freakchicken.gitee.io/images/dbApi/20220313/datasource_add.png)
![](https://freakchicken.gitee.io/images/dbApi/20220503/api_edit.png)
![](https://freakchicken.gitee.io/images/dbApi/20220503/api_edit2.png)
![](https://freakchicken.gitee.io/images/dbApi/20210803/sql_run.png)

![](https://freakchicken.gitee.io/images/dbApi/20210502/group.png)
![](https://freakchicken.gitee.io/images/dbApi/20221001/request.png)

![](https://freakchicken.gitee.io/images/dbApi/20221001/app_add.png)
![](https://freakchicken.gitee.io/images/dbApi/20221001/apps.png)
![](https://freakchicken.gitee.io/images/dbApi/20221001/auth.png)
![](https://freakchicken.gitee.io/images/dbApi/20210502/docs.png)
![](https://freakchicken.gitee.io/images/dbApi/20210803/ip.png)

## 使用说明

请阅读 [《详细使用说明》](./dbapi-assembly/docs/instruction.md)


## 插件开发
- 请阅读 [《插件开发指南》](./dbapi-assembly/docs/plugin%20development.md)
- 作者已经开发了字段加密插件和redis缓存插件，请阅读[案例demo](https://gitee.com/freakchicken/dbapi-plugin-demo)

## 二次开发
- 请阅读 [《开发指南》](./dbapi-assembly/docs/development.md)

## springboot集成

如果您想更加灵活的使用DBApi，在您自己的java springboot项目中使用代码配置接口，
请使用[dbApi-spring-boot-starter开源框架](https://gitee.com/freakchicken/dbApi-spring-boot-starter)


## 联系作者：

### 微信：
- [登记](https://gitee.com/freakchicken/db-api/issues/I4XLLW) 过的同学可以私聊作者获取一对一技术指导
- 提问请先star支持一下，提问前请先把文档读一遍，并阅读[常见问题汇总](https://gitee.com/freakchicken/db-api/issues/I4XLLJ)
- 加微信群请备注`dbapi加群`
<div style="text-align: center"> 
<img src="https://freakchicken.gitee.io/images/kafkaui/wechat.jpg" width = "30%" />
</div>

### 微信交流群：
- 在群里提问前请先把文档读一遍，并阅读[常见问题汇总](https://gitee.com/freakchicken/db-api/issues/I4XLLJ)
<div style="text-align: center"> 
<img src="https://freakchicken.gitee.io/images/dbApi/wechatGroup.png" width = "40%" />
</div>

### qq交流群：
- 在群里提问前请先把文档读一遍，并阅读[常见问题汇总](https://gitee.com/freakchicken/db-api/issues/I4XLLJ)
<div style="text-align: center"> 
<img src="https://freakchicken.gitee.io/images/dbApi/qqgroup.jpg" width = "40%" />
</div>

### 捐赠：

开源不易，用爱发电，如果此项目帮助到您，请作者喝一杯咖啡
<div style="text-align: center"> 
<img src="https://freakchicken.gitee.io/images/kafkaui/wechatpay.jpg" width = "30%" />
<img src="https://freakchicken.gitee.io/images/kafkaui/alipay.jpg" width = "29%" />
</div>
