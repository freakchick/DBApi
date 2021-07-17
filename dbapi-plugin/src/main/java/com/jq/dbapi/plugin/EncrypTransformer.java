package com.jq.dbapi.plugin;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.List;

public class EncrypTransformer implements TransformPlugin {
    @Override
    public Object transform(List<JSONObject> data) {

        data.stream().forEach(t -> {
            String phone = t.getString("phone");
            String s = DigestUtils.md5Hex(phone);
            t.put("phone", s);
        });
        return data;

    }
}
