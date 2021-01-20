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
    boolean sucess;

    public static  ResponseDto success(Object data){
        ResponseDto dto = new ResponseDto();
        dto.setData(data);
        dto.setSucess(true);
        dto.setMsg("接口访问成功");
        return dto;

    }

    public static  ResponseDto  fail(String msg){
        ResponseDto dto = new ResponseDto();
        dto.setSucess(false);
        dto.setMsg(msg);
        return dto;

    }
}
