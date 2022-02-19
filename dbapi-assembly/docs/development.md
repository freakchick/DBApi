## 开发指南
### 环境依赖

- 安装jdk8+
- 安装node.js
- 安装nacos
- (可选)npm设置国内源
```shell
npm config set registry https://registry.npm.taobao.org
```
### 编译打包

```shell script
mvn clean package -P release
```
### 构建镜像

```shell script
sh docker/build.sh
```
