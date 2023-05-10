package com.gitee.freakchicken.dbapi.common;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * @program: api
 * @description:
 * @author: jiangqiang
 * @create: 2020-08-11 11:22
 **/

public class ResponseDto {

    String msg;
    boolean success;

    @JSONField(serialzeFeatures = { SerializerFeature.WriteMapNullValue })
    Object data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static ResponseDto apiSuccess(Object data) {
        ResponseDto dto = new ResponseDto();
        dto.setData(data);
        dto.setSuccess(true);
        // dto.setMsg("Api access succeeded");
        return dto;

    }

    public static ResponseDto successWithMsg(String msg) {
        ResponseDto dto = new ResponseDto();
        dto.setData(null);
        dto.setSuccess(true);
        dto.setMsg(msg);
        return dto;
    }

    public static ResponseDto successWithData(Object data) {
        ResponseDto dto = new ResponseDto();
        dto.setData(data);
        dto.setSuccess(true);
        return dto;
    }

    public static ResponseDto fail(String msg) {
        ResponseDto dto = new ResponseDto();
        dto.setSuccess(false);
        dto.setMsg(msg);
        return dto;
    }

    public static ResponseDto failWithData(String msg, Object data) {
        ResponseDto dto = new ResponseDto();
        dto.setSuccess(false);
        dto.setMsg(msg);
        dto.setData(data);
        return dto;
    }
}
