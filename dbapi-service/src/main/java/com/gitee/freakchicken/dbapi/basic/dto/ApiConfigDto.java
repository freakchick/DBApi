package com.gitee.freakchicken.dbapi.basic.dto;

import java.util.List;
import com.gitee.freakchicken.dbapi.common.ApiPluginConfig;
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

    List<ApiPluginConfig> plugins; 
}
