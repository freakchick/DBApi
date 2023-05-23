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
     * @param data             执行器执行后返回的结果数据
     * @param localPluginParam 插件的局部参数
     * @return
     */
    public abstract Object transform(Object data, String localPluginParam);
}
