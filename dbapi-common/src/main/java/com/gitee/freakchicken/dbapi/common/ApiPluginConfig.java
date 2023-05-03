package com.gitee.freakchicken.dbapi.common;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
@TableName(value = "api_plugin_config")
public class ApiPluginConfig implements Serializable {

    @TableField("api_id")
    String apiId;

    @TableField("plugin_type")
    Integer pluginType;
    
    @TableField("plugin_name")
    String pluginName;

    @TableField("plugin_param")
    String pluginParam;
}