package com.jq.dbapi.plugin;

import com.jq.dbapi.common.ApiConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public abstract class CachePlugin {

    public Logger logger = LoggerFactory.getLogger(CachePlugin.class);

    /**
     * 缓存设置
     *
     * @param config api配置
     * @param params request参数
     * @param data   要缓存的数据
     */
    public abstract void set(ApiConfig config, Map<String, Object> params, Object data);

    /**
     * 清除所有缓存
     *
     * @param config api配置
     */
    public abstract void clean(ApiConfig config);

    /**
     * 查询缓存
     *
     * @param config api配置
     * @param params request参数
     * @return
     */
    public abstract Object get(ApiConfig config, Map<String, Object> params);
}
