# DBApi插件开发指南

## 1.插件的作用
- DBApi的插件分两类，一类是数据转换插件，一类是缓存插件

### 1.1 数据转换插件
- 有时候sql无法一次性获得自己想要的数据格式，如果用代码对数据进行一些处理转换能更加方便，这时候就要用到数据转换插件。用户自己编写数据转换逻辑的代码。
- 比如针对sql查询结果中的用户手机号、银行卡号进行转换脱敏。

### 1.2 缓存插件
- 主要是对查询类API，sql查询结果进行缓存，这样避免频繁的查询数据库，对数据库造成压力。
- 缓存逻辑由用户自己编写，用户可以缓存到redis/mangodb/elasticsearch等等。
- 当从缓存中查询不到数据时，才去数据库查询，同时将结果缓存下来。

## 2.插件的开发流程

### 2.1 准备工作
- 插件使用java语言编写，准备java(8+)开发环境
- 新建maven项目，pom里引入dbapi-plugin
```xml
<dependency>
    <groupId>com.gitee.freakchicken.dbapi</groupId>
    <artifactId>dbapi-plugin</artifactId>
    <version>2.3.2</version>
    <scope>provided</scope>
</dependency>
```

### 2.2 缓存插件开发
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

### 2.3 数据转换插件开发
- 新建java类，实现com.gitee.freakchicken.dbapi.plugin.TransformPlugin

```java
import com.alibaba.fastjson.JSONObject;
import com.gitee.freakchicken.dbapi.common.ApiConfig;
import com.gitee.freakchicken.dbapi.plugin.TransformPlugin;

import java.util.List;

public class Tdemo extends TransformPlugin {
    
    /**
     * sql查询结果数据转换
     * @param data sql查询出来的结果
     * @param config Api配置信息
     * @return
     */
    @Override
    public Object transform(List<JSONObject> data, ApiConfig config) {
        return null;
    }
}
```
- 数据转换逻辑写在transform方法里，第一个参数就是sql查询结果，第二个参数是API的配置

### 2.4 插件日志打印
- 如果想在插件内打印日志，推荐直接调用父类的logger
```java
super.logger.debug("set data to cache");
```

### 2.5 插件参数
#### 2.5.1 全局参数
- 插件全局参数是每个插件自身的参数，与API无关，比如缓存插件需要连接的redis的ip、端口信息。配置在文件conf/plugin.properties中。
- 全局参数主要是为了方便不同环境的切换，比如测试环境和生产环境需要连接不同的redis地址，那么把redis地址信息配在配置文件就很方便。
- 如果用户想添加插件的全局配置，可以直接在plugin.properties文件中添加配置，比如：
```properties
RedisCachePlugin.ip=127.0.0.1
RedisCachePlugin.port=6379
RedisCachePlugin.db=0
RedisCachePlugin.password=
```
- 代码获取插件全局参数值的方法：
```java
import com.gitee.freakchicken.dbapi.plugin.PluginConf;
String ip = PluginConf.getKey("RedisCachePlugin.ip")
```

#### 2.5.2 局部参数
- 插件局部参数是为每个API单独指定的参数，参数值从页面上配置进去（创建、编辑API的时候）。
- 比如针对redis缓存插件，不同的API需要设置不同的缓存时间， 那么每个API调用同一个缓存插件的时候，就可以传递不同的时间参数进去。
又比如针对字段加密插件，每个API需要加密的字段都不一样，那么每个API调用同一个字段加密插件的时候，就可以传递不同的字段名参数进去。
这样可以让一个插件灵活的被多个API复用。
- 代码获取插件局部参数值，从ApiConfig类中获取：
```java
import com.gitee.freakchicken.dbapi.common.ApiConfig;
String params = ApiConfig.getCachePluginParams();
```

### 2.6 插件使用
- 用户开发完插件后，请打包，将最后生成的jar包和插件依赖的jar包拷贝进DBApi的lib目录下，
再重启DBApi服务，就可以使用插件了
- 如果插件中使用了全局参数，还需要在conf/plugin.properties文件添加相应配置

## 3.插件开发完整案例
[案例demo](https://gitee.com/freakchicken/dbapi-plugin-demo)