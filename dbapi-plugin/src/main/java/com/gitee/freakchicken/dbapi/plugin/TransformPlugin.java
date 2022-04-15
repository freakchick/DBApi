package com.gitee.freakchicken.dbapi.plugin;

import com.alibaba.fastjson.JSONObject;
import com.gitee.freakchicken.dbapi.common.ApiConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class TransformPlugin implements BasePlugin  {

    public Logger logger = LoggerFactory.getLogger(CachePlugin.class);

    /**
     * 插件初始化方法，实例化插件的时候执行，永远只会执行一次，
     */
    public abstract void init();

    /**
     * 数据转换逻辑
     *
     * @param data   sql查询结果
     * @param params 缓存插件局部参数
     * @return
     */
    public abstract Object transform(List<JSONObject> data, String params);
}
