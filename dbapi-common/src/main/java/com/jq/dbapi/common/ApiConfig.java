package com.jq.dbapi.common;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @program: dbApi
 * @description:
 * @author: jiangqiang
 * @create: 2021-01-20 09:50
 **/
@Data
@TableName(value = "api_config")
public class ApiConfig {

    @TableId(value = "id", type = IdType.AUTO)
    Integer id;

    @TableField
    String name;

    @TableField
    String note;

    @TableField
    String path;

    @TableField(value = "datasource_id")
    Integer datasourceId;

    @TableField("`sql`")
    String sql;

    @TableField(value = "real_sql")
    String realSql;

    @TableField
    String params;

    @TableField
    Integer status;

    @TableField(value = "is_select")
    Integer isSelect;

    @TableField
    Integer previlege;

    @TableField("group_id")
    Integer groupId;

    @TableField(value = "cache_plugin")
    String cachePlugin;

    @TableField(value = "transform_plugin")
    String transformPlugin;

    @TableField(value = "cache_plugin_param")
    String cachePluginParam;

    @TableField(value = "transform_plugin_param")
    String transformPluginParam;
}
