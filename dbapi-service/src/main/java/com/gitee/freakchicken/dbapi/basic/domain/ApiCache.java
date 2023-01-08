package com.gitee.freakchicken.dbapi.basic.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "api_cache")
public class ApiCache {

    @TableField(value = "api_id")
    String apiId;

    @TableField("plugin")
    String plugin;

    @TableField("plugin_param")
    String pluginParam;
}
