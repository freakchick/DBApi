

# DBApi使用说明

## 数据源
- 理论上支持所有支持JDBC协议的数据库
- 创建数据源的时候如果选择其他类型，需要用户手动填写JDBC驱动class类和查询所有表的sql，**同时将相应的JDBC驱动jar包放入DBApi的lib目录下并重启生效**
- DBApi已经自带mysql/sqlserver/postgreSql/hive/kylin/clickhouse/的驱动包，**如果版本不匹配请手动替换lib目录下的相应驱动jar包**
- 查询所有表sql的作用：创建、编辑API的时候，点击选择数据源，会自动展示该数据源下的所有表名称，方便辅助用户编写业务逻辑sql。
由于不同数据库查询所有表名称的方式都不一样，所以需要用户手动去填写这个sql

## API配置元数据说明
|  配置项   | 说明  |备注|
|  ----  | ----  |----|
|id| API的唯一标识，自动生成|api结果缓存redis的时候可以以id作为redis key|
|名称| API的名称||
|请求路径| API的http请求的访问地址 ||
|API分组|API使用分组管理，创建API前要先创建好分组，然后把API归属到某个分组下||
|描述|API功能的详细描述||
|数据源|API中的sql执行的数据库地址，也是必须先创建好数据源，然后创建API的时候需要选择某个数据源|理论上支持所有类型的数据库（jdbc连接）|
|sql|API中的执行sql，支持类似mybatis的动态sql语法|不需要写最外层select/update标签，参数名用 #{}、${}表示，可以参考[这里](https://mybatis.org/mybatis-3/zh/dynamic-sql.html)|
|访问权限|开放接口可以直接访问，私有接口需要申请token才能访问|请在权限菜单中申请token|
|数据转换插件|对数据进行转换的插件的java类名（class全路径），如果没有值表示此API结果不需要转换|使用转换插件可以按照自定义代码逻辑对数据进行转换，比如字段加密脱敏|
|数据转换插件参数|数据转换插件需要的参数|比如针对字段加密插件，每个API需要加密的字段都不一样，那么每个API调用同一个字段加密插件的时候，就可以传递不同的字段名参数进去。|
|缓存插件|缓存插件的java类名（class全路径），如果没有值表示此API结果不需要开启缓存|使用缓存插件可以按自定义代码逻辑对数据进行缓存，以后每次API被访问的时候就不需要都执行sql，比如缓存到redis|
|缓存插件参数|缓存插件需要的参数|比如针对redis缓存插件，不同的API需要设置不同的缓存时间， 那么每个API调用同一个缓存插件的时候，就可以传递不同的时间参数进去。|

![](https://freakchicken.gitee.io/images/dbApi/20211011/api_add.png)

## 插件
- DBApi的插件分两类，一类是数据转换插件，一类是缓存插件
- 插件需要用户自己编写java代码开发。开发完成后打成jar包，并放入DBApi的lib目录下即可以使用
- [插件开发指南](./plugin%20development.md)

### 1. 数据转换插件
- 有时候sql无法一次性获得自己想要的数据格式，如果用代码对数据进行一些处理转换能更加方便，这时候就要用到数据转换插件。用户自己编写数据转换逻辑的代码。
- 典型场景，比如针对sql查询结果中的用户手机号、银行卡号进行加密脱敏。

### 2. 缓存插件
- 主要是对查询类API，sql查询结果进行缓存，这样避免频繁的查询数据库，对数据库造成压力。
- 缓存逻辑由用户自己编写，用户可以缓存到redis/mangodb/elasticsearch等等。
- 如果开启了缓存，当从缓存中查询不到数据时，才去数据库查询，同时将结果缓存下来。

## 基本操作

### 创建数据源

![](https://freakchicken.gitee.io/images/dbApi/20210502/datasource_create.png)

### 创建/修改api

![](https://freakchicken.gitee.io/images/dbApi/20211011/api_add.png)

- 请求路径，这就是将来http请求的路径
- 选择数据源，就是接口执行sql逻辑的数据库地址
- 填入sql ,类似mybatis的动态sql语法，不需要写最外层的select update 标签，参数名用 #{} ${}
  表示，可以参考[这里](https://mybatis.org/mybatis-3/zh/dynamic-sql.html)
- 添加参数，参数名称就是sql中的参数名，sql中涉及到的每个参数都要填写
- API分组，选择api所属的分组，这个分组可以将来授权使用
- 访问权限，开放接口可以直接访问，私有接口需要申请token才能访问
- 数据转换，如果需要数据转换，就填写数据转换插件的java类名，不填写表示不转换。插件如果需要传参数就填写参数。
- 缓存，如果需要数据缓存，就填写缓存插件的java类名，不填写表示不开启缓存。插件如果需要传参数就填写参数。
- 点击保存，返回api列表页面，可以看到新增一条记录

![](https://freakchicken.gitee.io/images/dbApi/20210803/api_list.png)

### api分组管理
- 可以添加、删除分组
  ![](https://freakchicken.gitee.io/images/dbApi/20210502/group.png)

### api请求测试
- 在页面快速访问API，查看结果
  ![](https://freakchicken.gitee.io/images/dbApi/20210502/request.png)

### sql调试
- 点击sql填写框右上角的最大化按钮，可以开启sql调试功能
![](https://freakchicken.gitee.io/images/dbApi/20210803/sql_run.png)
- 如果sql中有参数，在调试的时候，请输入参数

## 权限管理
### 创建token
![](https://freakchicken.gitee.io/images/dbApi/20210502/token_add.png)
![](https://freakchicken.gitee.io/images/dbApi/20210502/token.png)

### 授权token可以访问哪些api
![](https://freakchicken.gitee.io/images/dbApi/20210502/token_auth.png)

### ip防火墙设置
![](https://freakchicken.gitee.io/images/dbApi/20210803/ip.png)
## 其他功能
### 导出接口文档
- 可以导出接口文档（markdown格式）
  ![](https://freakchicken.gitee.io/images/dbApi/20210502/docs.png)



## sql编写规范

- 和mybatis动态sql语法一样，同样支持参数#{}、 ${}，可以参考[这里](https://mybatis.org/mybatis-3/zh/dynamic-sql.html)
，不需要写最外层的select update 标签，直接写sql内容
- 注意和mybatis一样，sql里的小于号不要写成<，要写成 <![CDATA[&lt;]]>