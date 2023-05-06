package com.gitee.freakchicken.dbapi.basic.executor;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public interface Executor {
    
    public Object execute(JSONObject taskJson, Map<String, Object> param) throws Exception;
}
