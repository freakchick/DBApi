

## DBApi使用说明

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