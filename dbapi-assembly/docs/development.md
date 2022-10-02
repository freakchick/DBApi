## 开发指南
### 环境依赖

- 安装jdk8+
- 安装node.js（推荐v12.15.0）
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

### 注意事项

 请使用最新的发行版本的代码，不要使用dev分支代码，dev分支不保证可运行

### 附：
- [nacos版本说明](https://github.com/alibaba/spring-cloud-alibaba/wiki/%E7%89%88%E6%9C%AC%E8%AF%B4%E6%98%8E) 
