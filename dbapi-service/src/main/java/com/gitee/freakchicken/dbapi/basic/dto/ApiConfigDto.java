package com.gitee.freakchicken.dbapi.basic.dto;

import lombok.Data;

@Data
public class ApiConfigDto {
    String id;

    String name;

    String note;

    String path;
    String params;

    Integer status;

    Integer previlege;

    String groupId;

    String contentType;

    String jsonParam;

    String taskType;

    String task;

    String createTime;

    String updateTime;

    String alarmPluginName;
    String alarmPluginParam;
    String cachePluginName;
    String cachePluginParam;
}
