package com.jq.dbapi.plugin;

import com.alibaba.fastjson.JSONObject;
import com.jq.dbapi.util.ResponseDto;

import java.util.List;

public interface DataTransformer {

    Object transform(List<JSONObject> data);
}
