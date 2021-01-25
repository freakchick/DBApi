package com.jq.dbapi.util;

import lombok.Data;

/**
 * @program: api
 * @description:
 * @author: jiangqiang
 * @create: 2020-08-11 11:22
 **/
@Data
public class ResponseDto {
    String msg;
    Object data;
    boolean success;

    public static ResponseDto apiSuccess(Object data) {
        ResponseDto dto = new ResponseDto();
        dto.setData(data);
        dto.setSuccess(true);
        dto.setMsg("接口访问成功");
        return dto;

    }

    public static ResponseDto success(String msg) {
        ResponseDto dto = new ResponseDto();
        dto.setData(null);
        dto.setSuccess(true);
        dto.setMsg(msg);
        return dto;

    }

    public static ResponseDto fail(String msg) {
        ResponseDto dto = new ResponseDto();
        dto.setSuccess(false);
        dto.setMsg(msg);
        return dto;

    }
}
