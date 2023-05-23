package com.gitee.freakchicken.dbapi.plugin;

import java.util.Map;

import com.gitee.freakchicken.dbapi.common.ApiConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ParameterValidator implements BasePlugin {

    public Logger logger = LoggerFactory.getLogger(ParameterValidator.class);

    /**
     * 插件初始化方法，实例化插件的时候执行，永远只会执行一次，
     */
    public abstract void init();

    /**
     * 请求参数校验逻辑
     *
     * @param requestParameter API请求参数
     * @param apiConfig        API元数据
     * @param clientId         客户端ID
     * @param localPluginParam 插件的局部参数
     * @return
     */
    public abstract boolean validate(Map<String, Object> requestParameter, ApiConfig apiConfig, String clientId,
            String localPluginParam);
}
