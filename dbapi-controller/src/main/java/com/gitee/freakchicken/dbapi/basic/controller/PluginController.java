package com.gitee.freakchicken.dbapi.basic.controller;

import com.alibaba.fastjson.JSONObject;
import com.gitee.freakchicken.dbapi.plugin.PluginManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/plugin")
public class PluginController {

    @RequestMapping("/all")
    public JSONObject getAllCachePlugin(){
        Set<String> allCachePlugin = PluginManager.getAllCachePlugin();
        Set<String> allTransformPlugin = PluginManager.getAllTransformPlugin();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cache",allCachePlugin);
        jsonObject.put("transform",allTransformPlugin);
        return jsonObject;
    }

}
