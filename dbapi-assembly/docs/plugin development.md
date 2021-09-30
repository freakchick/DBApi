# DBApi插件开发指南

## 插件的作用
DBApi的插件分两类，一类是数据转换插件，一类是缓存插件

### 数据转换插件
主要是对查询类API，sql查询结果进行转换。典型应用场景是数据脱敏，
比如针对sql查询结果中的用户手机号、银行卡号进行转换脱敏。

### 缓存插件
主要是对查询类API，sql查询结果进行缓存，这样避免频繁的查询数据库，对数据库造成压力。
缓存逻辑由用户自己编写，用户可以缓存到redis/mangodb/elasticsearch等等。
当从缓存结果中查询不到数据时，才去数据库查询，同时将结果缓存下来。

## 插件的开发流程

### 准备工作
- 插件使用java语言编写，准备java(8+)开发环境和node.js环境
- 新建maven项目，pom里引入dbapi-plugin
```xml
<dependency>
    <groupId>com.gitee.freakchicken.dbapi</groupId>
    <artifactId>dbapi-plugin</artifactId>
    <version>2.3.1</version>
    <scope>provided</scope>
</dependency>
```

### 缓存插件开发
- 新建java类实现com.gitee.freakchicken.dbapi.plugin.CachePlugin
```java
import com.gitee.freakchicken.dbapi.common.ApiConfig;
import com.gitee.freakchicken.dbapi.plugin.CachePlugin;

import java.util.Map;

public class Cdemo extends CachePlugin {
  /**
   * 插件初始化方法，实例化插件的时候执行，永远只会执行一次，
   * 一般是用来创建连接池
   */
  @Override
  public void init() {

  }

  /**
   * 缓存设置
   *
   * @param config api配置
   * @param params request参数
   * @param data   要缓存的数据
   */
  @Override
  public void set(ApiConfig config, Map<String, Object> params, Object data) {

  }

  /**
   * 清除所有缓存，API修改、删除、下线的时候会触发清除缓存
   *
   * @param config api配置
   */
  @Override
  public void clean(ApiConfig config) {

  }

  /**
   * 查询缓存
   *
   * @param config api配置
   * @param params request参数
   * @return
   */
  @Override
  public Object get(ApiConfig config, Map<String, Object> params) {
    return null;
  }
}


```

- init方法是插件初始化方法，只会执行一次，一般用来初始化连接池，比如初始化redis连接池
- get方法是获取缓存中的数据的方法，第一个参数是api配置，第二个参数是请求的参数
- set方法是设置缓存的方法，当执行sql查询结果后，会调用set方法。第一个参数是api配置，
  第二个参数是请求的参数，第三个参数是sql查询结果，如果api配置了数据转换插件的话，就是转换后的结果
- clean是清空缓存的方法，当API的修改、下线、删除的时候，会执行这个clean方法

### 数据转换插件开发
- 新建java类，实现com.gitee.freakchicken.dbapi.plugin.TransformPlugin

```java
import com.alibaba.fastjson.JSONObject;
import com.gitee.freakchicken.dbapi.plugin.TransformPlugin;

import java.util.List;

public class Tdemo extends TransformPlugin {
    /**
     * sql查询结果数据转换
     *
     * @param data
     * @return
     */
    @Override
    public Object transform(List<JSONObject> data) {
        return null;
    }
}
```
- 数据转换逻辑写在transform方法里，第一个参数就是sql查询结果

### 公用API
#### 日志打印
- 如果想在插件内打印日志，直接调用父类的logger
```java
super.logger.debug("set data to cache");
```

#### 插件参数配置
- DBApi 自带一个配置文件conf/plugin.properties
- 如果用户想把插件相关的配置写在配置文件，可以直接在plugin.properties文件中添加配置
- 并使用PluginConf.getKey("xxx")方法来获取参数

### 插件使用
- 用户开发完插件后，请打包，将最后生成的jar包和插件依赖的jar包拷贝进DBApi的lib目录下，
再重启DBApi服务，就可以使用插件了
- 如果插件中使用了外部配置，还需要在conf/plugin.properties添加相应配置

### 插件开发完整案例
[案例demo](https://gitee.com/freakchicken/dbapi-plugin-demo)