package com.gitee.freakchicken.dbapi.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gitee.freakchicken.dbapi.common.ResponseDto;

public abstract class GlobalTransformPlugin implements BasePlugin {

    public Logger logger = LoggerFactory.getLogger(GlobalTransformPlugin.class);

    /**
     * 插件初始化方法，实例化插件的时候执行，永远只会执行一次，
     */
    public abstract void init();

    /**
     * 数据转换逻辑
     *
     * @param data   API返回数据
     * @param params 全局转换插件的局部参数
     * @return
     */
    public abstract Object transform(ResponseDto data, String params);
}
