## 开发指南
### 环境依赖

- 安装jdk8+
- 安装node.js
- 安装nacos （推荐1.4.2）
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

### 附：
- [nacos版本说明](https://github.com/alibaba/spring-cloud-alibaba/wiki/%E7%89%88%E6%9C%AC%E8%AF%B4%E6%98%8E) 
