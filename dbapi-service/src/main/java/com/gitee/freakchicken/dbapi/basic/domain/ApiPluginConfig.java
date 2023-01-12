package com.gitee.freakchicken.dbapi.basic.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "api_plugin_config")
public class ApiPluginConfig implements Serializable {

    @TableField("api_id")
    String apiId;

    @TableField("plugin_type")
    String pluginType;
    
    @TableField("plugin_name")
    String pluginName;

    @TableField("plugin_param")
    String pluginParam;
}