package com.gitee.freakchicken.dbapi.plugin.impl;

import com.gitee.freakchicken.dbapi.common.ResponseDto;
import com.gitee.freakchicken.dbapi.plugin.GlobalTransformPlugin;

public class AmisGlobalTransformPlugin extends GlobalTransformPlugin {
    @Override
    public void init() {
        System.out.println("------demo transform------");
    }

    @Override
    public Object transform(ResponseDto data, String params) {
        return null;
    }

    @Override
    public String getName() {
        return "Amis格式转换插件";
    }

    @Override
    public String getDescription() {
        return "demo数据转换插件描述";
    }

    @Override
    public String getParamDescription() {
        return "demo数据转换参数插件";
    }
}
