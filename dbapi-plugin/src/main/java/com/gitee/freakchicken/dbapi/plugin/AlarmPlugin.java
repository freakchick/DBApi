package com.gitee.freakchicken.dbapi.plugin;

import com.gitee.freakchicken.dbapi.common.ApiConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

public abstract class AlarmPlugin implements BasePlugin {

    public Logger logger = LoggerFactory.getLogger(AlarmPlugin.class);

    /**
     * 插件初始化方法，实例化插件的时候执行，永远只会执行一次
     */
    public abstract void init();

    public abstract void alarm(Exception e, ApiConfig config, HttpServletRequest request, String pluginParam);

}
