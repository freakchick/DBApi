package com.gitee.freakchicken.dbapi.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class TransformPlugin implements BasePlugin {

    public Logger logger = LoggerFactory.getLogger(TransformPlugin.class);

    /**
     * 插件初始化方法，实例化插件的时候执行，永远只会执行一次，
     */
    public abstract void init();

    /**
     * 数据转换逻辑
     *
     * @param data   if SQL Executor , the data type is List<JSONObject>
     * @param params 局部转换插件的局部参数
     * @return
     */
    public abstract Object transform(Object data, String params);
}
