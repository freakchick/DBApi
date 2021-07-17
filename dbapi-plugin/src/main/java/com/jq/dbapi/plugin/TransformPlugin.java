package com.jq.dbapi.plugin;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface TransformPlugin {

    Object transform(List<JSONObject> data);
}
