package com.jq.dbapi.plugin;

import com.jq.dbapi.common.ApiConfig;

import java.util.Map;

public interface DataCacher {

    void set(ApiConfig config, Map<String, Object> params, Object data);

    Object get(ApiConfig config, Map<String, Object> params);
}
