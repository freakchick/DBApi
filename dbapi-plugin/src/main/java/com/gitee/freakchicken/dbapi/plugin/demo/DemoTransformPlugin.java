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
}
