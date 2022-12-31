package com.gitee.freakchicken.dbapi.basic.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@TableName(value = "sql_api_config")
public class SQLApiConfig {
    
    @TableField("api_id")
    String apiId;

    @TableField("open_trans")
    Integer openTrans;

    @TableField
    String alarmPlugin;

    @TableField
    String alarmPluginParam;

    @TableField(value = "cache_plugin", insertStrategy = FieldStrategy.NOT_EMPTY)
    String cachePlugin;

    /**
     * 缓存插件参数
     */
    @TableField(value = "cache_plugin_params", updateStrategy = FieldStrategy.IGNORED)
    String cachePluginParams;

    @TableField(value = "create_time")
    String createTime;

    @TableField(value = "update_time")
    String updateTime;

    @TableField(exist = false)
    List<ApiSql> sqlList;
}
