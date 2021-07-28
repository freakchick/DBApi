package com.jq.dbapi.plugin;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.List;

/**
 * md5字段加密插件
 */
public class EncryptTransformerPlugin extends TransformPlugin {
    @Override
    public Object transform(List<JSONObject> data) {
        String[] columns = PluginConf.getKey("EncryptTransformer.columns").split(";");

        data.stream().forEach(t -> {
            for (String column : columns) {
                t.put(column, DigestUtils.md5Hex(t.getString(column)));
            }
        });
        return data;

    }
}
