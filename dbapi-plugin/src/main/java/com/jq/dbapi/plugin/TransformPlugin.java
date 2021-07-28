package com.jq.dbapi.plugin;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class TransformPlugin {

    Logger logger = LoggerFactory.getLogger(CachePlugin.class);

    /**
     * sql查询结果数据转换
     *
     * @param data
     * @return
     */
    public abstract Object transform(List<JSONObject> data);
}
