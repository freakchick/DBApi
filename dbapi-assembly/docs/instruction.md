

# DBApi使用说明

## 基本操作

### 创建数据源

![](https://freakchicken.gitee.io/images/dbApi/20210502/datasource_create.png)

### 创建/修改api

![](https://freakchicken.gitee.io/images/dbApi/20210803/api_add.png)
![](https://freakchicken.gitee.io/images/dbApi/20210803/api_edit.png)

- 请求路径，这就是将来http请求的路径
- 选择数据源，就是接口执行sql逻辑的数据库地址
- 填入sql ,类似mybatis的动态sql语法，不需要写最外层的select update 标签，参数名用 #{} ${}
  表示，可以参考[这里](https://mybatis.org/mybatis-3/zh/dynamic-sql.html)
- 添加参数，参数名称就是sql中的参数名，sql中涉及到的每个参数都要填写
- API分组，选择api所属的分组，这个分组可以将来授权使用
- 访问权限，开放接口可以直接访问，私有接口需要申请token才能访问
- 数据转换，如果需要数据转换，就填写数据转换插件的java类名，不填写表示不转换
- 缓存，如果需要数据缓存，就填写缓存插件的java类名，不填写表示不开启缓存
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