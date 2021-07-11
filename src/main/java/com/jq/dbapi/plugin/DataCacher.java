package com.jq.dbapi.plugin;

import com.jq.dbapi.domain.ApiConfig;

import java.util.Map;

public interface DataCacher {

    public void set(ApiConfig config, Map<String,Object> params,Object data);

    public Object get(ApiConfig config, Map<String,Object> params);
}
