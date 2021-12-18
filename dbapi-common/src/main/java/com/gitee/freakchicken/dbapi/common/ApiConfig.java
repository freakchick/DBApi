package com.gitee.freakchicken.dbapi.common;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

/**
 * @program: dbApi
 * @description:
 * @author: jiangqiang
 * @create: 2021-01-20 09:50
 **/
@Data
@TableName(value = "api_config")
public class ApiConfig {

    @TableId(value = "id")
    String id;

    @TableField
    String name;

    @TableField
    String note;

    @TableField
    String path;

    @TableField(value = "datasource_id")
    String datasourceId;

    @TableField(exist = false)
    List<ApiSql> sqlList;

    @TableField
    String params;

    @TableField
    Integer status;

    @TableField
    Integer previlege;

    @TableField("group_id")
    String groupId;

    @TableField(value = "cache_plugin")
    String cachePlugin;

    @TableField(value = "transform_plugin")
    String transformPlugin;

    /**
     * 缓存插件参数
     */
    @TableField(value = "cache_plugin_params")
    String cachePluginParams;

    /**
     * 数据转换插件参数
     */
    @TableField(value = "transform_plugin_params")
    String transformPluginParams;

    @TableField(value = "create_time")
    String createTime;

    @TableField(value = "update_time")
    String updateTime;

}
