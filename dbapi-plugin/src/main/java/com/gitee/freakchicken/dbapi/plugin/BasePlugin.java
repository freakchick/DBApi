package com.gitee.freakchicken.dbapi.plugin;

public interface BasePlugin {

    /**
     * 插件名称，用于在页面上显示，提示用户
     * @return
     */
    String getName();

    /**
     * 插件功能描述，用于在页面上显示，提示用户
     * @return
     */
    String getDescription();

    /**
     * 插件参数描述，用于在页面上显示，提示用户
     * @return
     */
    String getParamDescription();
}
