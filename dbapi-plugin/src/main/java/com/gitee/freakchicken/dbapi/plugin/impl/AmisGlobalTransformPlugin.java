package com.gitee.freakchicken.dbapi.plugin.impl;

import com.alibaba.fastjson.JSONObject;
import com.gitee.freakchicken.dbapi.common.ResponseDto;
import com.gitee.freakchicken.dbapi.plugin.GlobalTransformPlugin;

public class AmisGlobalTransformPlugin extends GlobalTransformPlugin {
    @Override
    public void init() {

    }

    /**
     * 返回AMIS框架指定的数据格式
     */
    @Override
    public Object transform(ResponseDto data, String params) {
        JSONObject obj = new JSONObject();
        if (data.getSuccess()) {
            obj.put("status", 0);
            obj.put("msg", data.getMsg());
            obj.put("data", data.getData());
        } else {
            obj.put("status", -1);
            obj.put("msg", data.getMsg());
            obj.put("data", data.getData());
        }
        
        return obj;
    }

    @Override
    public String getName() {
        return "AMIS格式转换插件";
    }

    @Override
    public String getDescription() {
        return "将数据格式转化成AMIS框架需要的格式：{\"msg\":\"xxx\",\"status\":0,\"data\":{\"key\":\"value\"}}";
    }

    @Override
    public String getParamDescription() {
        return "不需要填参数";
    }
}
