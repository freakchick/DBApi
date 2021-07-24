package com.jq.dbapi.plugin;

import com.jq.dbapi.common.ApiConfig;

import java.util.Map;

/**
 * redis缓存插件
 */
public class RedisCachePlugin implements CachePlugin {

    @Override
    public void set(ApiConfig config, Map<String, Object> params, Object data) {

    }

    @Override
    public void clean(ApiConfig config) {

    }

    @Override
    public Object get(ApiConfig config, Map<String, Object> params) {
        return null;
    }
}
