package com.gitee.freakchicken.dbapi.basic.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "es_api_config")
public class ESApiConfig implements Serializable {

    @TableField("api_id")
    String apiId;

    @TableField
    String method;

    @TableField
    String endpoint;

    @TableField
    String body;

    @TableField
    String alarmPlugin;

    @TableField
    String alarmPluginParam;

    @TableField(value = "cache_plugin", insertStrategy = FieldStrategy.NOT_EMPTY)
    String cachePlugin;

    @TableField(value = "cache_plugin_params", updateStrategy = FieldStrategy.IGNORED)
    String cachePluginParams;

    @TableField(value = "transform_plugin", insertStrategy = FieldStrategy.NOT_EMPTY)
    String transformPlugin;

    @TableField(value = "transform_plugin_params")
    String transformPluginParams;

}
