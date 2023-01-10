package com.gitee.freakchicken.dbapi.basic.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "api_alarm_config")
public class ApiAlarmConfig implements Serializable {

    @TableField("api_id")
    String apiId;
    
    @TableField("plugin_name")
    String pluginName;

    @TableField("plugin_param")
    String pluginParam;
}