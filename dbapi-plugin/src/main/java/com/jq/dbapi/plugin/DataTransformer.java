package com.jq.dbapi.plugin;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface DataTransformer {

    Object transform(List<JSONObject> data);
}
