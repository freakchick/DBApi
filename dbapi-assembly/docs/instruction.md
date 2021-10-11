

# DBApi使用说明

## API配置元数据说明
|  配置项   | 说明  |备注|
|  ----  | ----  |----|
|名称| API的名称||
|请求路径| API的http请求的访问地址 ||
|API分组|API使用分组管理，创建API前要先创建好分组，然后把API归属到某个分组下||
|描述|API功能的详细描述||
|数据源|API中的sql执行的数据库地址，也是必须先创建好数据源，然后创建API的时候需要选择某个数据源||
|sql|API中的执行sql，支持类似mybatis的动态sql语法|不需要写最外层select/update标签，参数名用 #{} ${}表示，可以参考[这里](https://mybatis.org/mybatis-3/zh/dynamic-sql.html)|
|访问权限|开放接口可以直接访问，私有接口需要申请token才能访问|请在权限菜单中申请token|
|数据转换插件|||
|缓存插件|||

![](https://freakchicken.gitee.io/images/dbApi/20211011/api_add.png)


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