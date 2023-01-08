package com.gitee.freakchicken.dbapi.basic.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "api_alarm")
public class ApiAlarm implements Serializable {

    @TableField("api_id")
    String apiId;
    
    @TableField("plugin")
    String plugin;

    @TableField("plugin_param")
    String pluginParam;
}