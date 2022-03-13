

# DBApi用户使用手册

## 数据源
- 理论上支持所有支持JDBC协议的数据库
- 创建数据源的时候如果选择其他类型，需要用户手动填写JDBC驱动class类和查询所有表的sql，**同时将相应的JDBC驱动jar包放入DBApi的`lib`目录下并重启生效（如果是集群模式，每个节点都需要拷贝jar包并重启集群）**
- DBApi已经自带mysql/sqlserver/postgreSql/hive/kylin/clickhouse/的驱动包，**如果版本不匹配请手动替换`lib`目录下的相应驱动jar包**
- `查询所有表sql`的作用：创建、编辑API的时候，点击选择数据源，会自动展示该数据源下的所有表名称，方便辅助用户编写业务逻辑sql。
由于不同数据库查询所有表名称的方式都不一样，所以需要用户手动去填写这个sql


![](https://freakchicken.gitee.io/images/dbApi/20220313/API_add.png)

## 插件
- DBApi的插件分两类，一类是数据转换插件，一类是缓存插件
- 插件需要用户自己编写java代码开发。开发完成后打成jar包，并放入DBApi的`lib`目录下即可以使用
- [插件开发指南](./plugin%20development.md)

### 1. 数据转换插件
- 有时候sql无法一次性获得自己想要的数据格式，如果用代码对数据进行一些处理转换能更加方便，这时候就要用到数据转换插件。用户自己编写数据转换逻辑的代码。
- 典型场景，比如针对sql查询结果中的用户手机号、银行卡号进行加密脱敏。

### 2. 缓存插件
- 主要是对查询类API，sql查询结果进行缓存，这样避免频繁的查询数据库，对数据库造成压力。
- 缓存逻辑由用户自己编写，用户可以缓存到redis/mongodb/elasticsearch等等。
- 如果开启了缓存，当从缓存中查询不到数据时，才去数据库查询，同时将结果缓存下来。

## 基本操作

### 创建/修改数据源

![](https://freakchicken.gitee.io/images/dbApi/20220313/datasource_add.png)

### 创建/修改API

![](https://freakchicken.gitee.io/images/dbApi/20220313/API_add.png)
![](https://freakchicken.gitee.io/images/dbApi/20220313/API_add_high.png)

- 请求路径，这就是将来http请求的路径
- 选择数据源，就是接口执行sql逻辑的数据库地址
- 填入sql ,类似mybatis的动态sql语法，**不需要写最外层的`<select>` `<update>` 标签**，参数名用 `#{}` `${}`
  表示，可以参考[mybatis文档](https://mybatis.org/mybatis-3/zh/dynamic-sql.html)
- 一个sql编写窗口内只能写一条sql
- 点击添加可以新增sql，一个API内可以执行多条sql，最后的多个结果封装后一起返回，比如分页查询，一个接口内既要查询详情也要查询总条数
- 添加参数，参数名称就是sql中的参数名，sql中涉及到的每个参数都要填写
- API分组，选择API所属的分组，这个分组可以将来授权使用
- 访问权限，开放接口可以直接访问，私有接口需要申请token才能访问
- 数据转换，如果需要数据转换，就填写数据转换插件的java类名，不填写表示不转换。插件如果需要传参数就填写参数。
**如果一个API内包含多条sql，那么每条sql会对应一个数据转换插件配置，数据转换插件永远是针对单条sql查询结果进行转换**
- 缓存，如果需要数据缓存，就填写缓存插件的java类名，不填写表示不开启缓存。插件如果需要传参数就填写参数。

### API分组管理
- 可以添加、删除分组
  ![](https://freakchicken.gitee.io/images/dbApi/20210502/group.png)

### API请求测试
- 在页面快速访问API，查看结果，注意如果是内网部署，您需要手动修改访问的IP、端口为外网IP、端口
  ![](https://freakchicken.gitee.io/images/dbApi/20210502/request.png)

### sql调试
- 点击sql填写框右上角的最大化按钮，可以开启sql调试功能
![](https://freakchicken.gitee.io/images/dbApi/20210803/sql_run.png)
- 如果sql中有参数，在调试的时候，请输入参数

## 权限管理
### 创建token
![](https://freakchicken.gitee.io/images/dbApi/20210502/token_add.png)
![](https://freakchicken.gitee.io/images/dbApi/20210502/token.png)

### 授权token可以访问哪些API
![](https://freakchicken.gitee.io/images/dbApi/20210502/token_auth.png)

### ip防火墙设置
![](https://freakchicken.gitee.io/images/dbApi/20210803/ip.png)
## 其他功能
### 导出接口文档
- 可以导出接口文档（markdown格式）
  ![](https://freakchicken.gitee.io/images/dbApi/20210502/docs.png)

## Token使用说明

- 请求私有接口时，需要把token值放入header的`Authorization`字段中携带，才可以访问成功。（如果是开放接口，不需要设置header）
- 以python为例，访问API的代码示例如下：

```python
import requests
headers = {"Authorization": "5ad0dcb4eb03d3b0b7e4b82ae0ba433f"}
re = requests.post("http://127.0.0.1:8520/API/userById", {"idList": [1, 2]}, headers=headers)
print(re.text)
```

## 注意事项
### 数据源支持
- **如果您要使用Oracle或者其他类型的数据源，请将相应的jdbc驱动包手动放入DBApi部署后的`lib`目录下（如果是集群部署每个节点都需要手动放入jar包）**
### sql编写规范
- 和mybatis动态sql语法一样，同样支持参数`#{}`、 `${}`，可以参考[mybatis文档](https://mybatis.org/mybatis-3/zh/dynamic-sql.html)
，不需要写最外层的`<select>` `<update>` 标签，直接写sql内容
- 注意和mybatis一样，sql里的小于号不要写成`<`，要写成 `&lt;`

## 权限校验流程
  ![](https://freakchicken.gitee.io/images/dbApi/20210502/lc.png)