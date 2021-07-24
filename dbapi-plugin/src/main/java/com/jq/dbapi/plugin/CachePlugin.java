package com.jq.dbapi.plugin;

import com.jq.dbapi.common.ApiConfig;

import java.util.Map;

public interface CachePlugin {

    /**
     * 缓存设置
     * @param config api配置
     * @param params request参数
     * @param data 要缓存的数据
     */
    void set(ApiConfig config, Map<String, Object> params, Object data);

    /**
     * 清除所有缓存
     * @param config api配置
     */
    void clean(ApiConfig config);

    /**
     * 查询缓存
     * @param config api配置
     * @param params request参数
     * @return
     */
    Object get(ApiConfig config, Map<String, Object> params);
}
