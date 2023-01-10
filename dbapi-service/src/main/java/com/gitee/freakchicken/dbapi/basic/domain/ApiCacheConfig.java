package com.gitee.freakchicken.dbapi.basic.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "api_cache_config")
public class ApiCacheConfig {

    @TableField(value = "api_id")
    String apiId;

    @TableField("plugin_name")
    String pluginName;

    @TableField("plugin_param")
    String pluginParam;
}
