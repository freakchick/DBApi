package com.gitee.freakchicken.dbapi.common;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @program: dbApi
 * @description:
 * @author: jiangqiang
 * @create: 2021-01-20 09:50
 **/
@Data
@TableName(value = "api_config")
public class ApiConfig implements Serializable {

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

    // @TableField(exist = false)
    // List<ApiSql> sqlList;

    /**
     * application/x-www-form-urlencoded 类API对应的参数
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    String params;

    @TableField
    Integer status;

    @TableField
    Integer previlege;

    @TableField("group_id")
    String groupId;

    // @TableField(value = "cache_plugin", insertStrategy = FieldStrategy.NOT_EMPTY)
    // String cachePlugin;

    /**
     * 缓存插件参数
     */
    // @TableField(value = "cache_plugin_params", updateStrategy = FieldStrategy.IGNORED)
    // String cachePluginParams;

    @TableField(value = "create_time")
    String createTime;

    @TableField(value = "update_time")
    String updateTime;

    @TableField(value = "content_type")
    String contentType;

    /**
     * 是否打开事务，1-是；0-否
     */
    // @TableField("open_trans")
    // Integer openTrans;

    /**
     * application/json 类API对应的json参数示例
     */
    @TableField(value = "json_param", updateStrategy = FieldStrategy.IGNORED)
    String jsonParam;

    // @TableField(exist = false)
    // String alarmPlugin;

    // @TableField(exist = false)
    // String alarmPluginParam;
}
