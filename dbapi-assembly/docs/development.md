## 开发指南
### 环境依赖

- 安装jdk8+
- 安装node.js
- (可选)npm设置国内源
```shell
npm config set registry https://registry.npm.taobao.org
```
### 编译打包

```shell script
mvn clean package -P release
```

### 构建镜像

- 在`dbapi-assembly`目录下

```shell script
mvn docker:build
mvn docker:push
```

### 启动

- 前端启动
`dbapi-ui` 目录下 `npm run serve`
- 后端启动:
`dbapi-core`目录下启动主类`com.gitee.freakchicken.DBApiManager`
- 前端访问地址：
```
http://localhost:8521
```
- 后端接口访问地址：
```
http://localhost:8520
```