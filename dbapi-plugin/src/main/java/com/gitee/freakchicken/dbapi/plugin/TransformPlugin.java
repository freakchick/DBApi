package com.gitee.freakchicken.dbapi.plugin;

import com.alibaba.fastjson.JSONObject;
import com.gitee.freakchicken.dbapi.common.ApiConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class TransformPlugin {

    Logger logger = LoggerFactory.getLogger(TransformPlugin.class);

    /**
     * 数据转换逻辑
     * @param data sql查询结果
     * @param config api配置
     * @return
     */
    public abstract Object transform(List<JSONObject> data, ApiConfig config);
}
