package com.gitee.freakchicken.dbapi.plugin;

import com.gitee.freakchicken.dbapi.common.ApiConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public abstract class CachePlugin implements BasePlugin {

    public Logger logger = LoggerFactory.getLogger(CachePlugin.class);

    /**
     * 插件初始化方法，实例化插件的时候执行，永远只会执行一次，
     * 一般是用来创建连接池
     */
    public abstract void init();

    /**
     * 缓存设置
     *
     * @param config           api配置
     * @param requestParams    request参数
     * @param data             要缓存的数据
     * @param localPluginParam 插件的局部参数
     */
    public abstract void set(ApiConfig config, Map<String, Object> requestParams, Object data, String localPluginParam);

    /**
     * 清除所有缓存，API修改、删除、下线的时候会触发清除缓存
     *
     * @param config           api配置
     * @param localPluginParam 插件的局部参数
     */
    public abstract void clean(ApiConfig config, String localPluginParam);

    /**
     * 查询缓存
     *
     * @param config           api配置
     * @param requestParams    request参数
     * @param localPluginParam 插件的局部参数
     * @return
     */
    public abstract Object get(ApiConfig config, Map<String, Object> requestParams, String localPluginParam);
}
