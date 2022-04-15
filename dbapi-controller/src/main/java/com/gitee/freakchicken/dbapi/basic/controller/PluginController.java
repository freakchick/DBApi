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
        jsonObject.put("cache", PluginManager.getAllCachePlugin());
        jsonObject.put("transform", PluginManager.getAllTransformPlugin());
        jsonObject.put("alarm", PluginManager.getAllAlarmPlugin());
        return jsonObject;
    }

}
