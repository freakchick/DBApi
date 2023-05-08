package com.gitee.freakchicken.dbapi.basic.controller;

import com.alibaba.fastjson.JSONObject;
import com.gitee.freakchicken.dbapi.plugin.PluginManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/plugin")
public class PluginController {

    @RequestMapping("/all")
    public JSONObject getAllCachePlugin() {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cache", PluginManager.getAllCachePlugins());
        jsonObject.put("transform", PluginManager.getAllTransformPlugins());
        jsonObject.put("alarm", PluginManager.getAllAlarmPlugins());
        jsonObject.put("globalTransform", PluginManager.getAllGlobalTransformPlugins());
        return jsonObject;
    }

}
