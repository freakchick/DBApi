package com.gitee.freakchicken.dbapi.plugin.demo;

import com.alibaba.fastjson.JSONObject;
import com.gitee.freakchicken.dbapi.plugin.TransformPlugin;

import java.util.List;

public class DemoTransformPlugin extends TransformPlugin {
    @Override
    public void init() {
        System.out.println("------demo transform------");
    }

    @Override
    public Object transform(List<JSONObject> data, String params) {
        return null;
    }

    @Override
    public String getName() {
        return "demo数据转换插件";
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
