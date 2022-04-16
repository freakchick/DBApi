package com.gitee.freakchicken.dbapi.basic.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName(value = "api_alarm")
public class Alarm {

    @TableField(value = "api_id")
    String apiId;
    
    @TableField("alarm_plugin")
    String alarmPlugin;

    @TableField("alarm_plugin_param")
    String alarmPluginParam;
}