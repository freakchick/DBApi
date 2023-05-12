package com.gitee.freakchicken.dbapi.common;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
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
public class ApiConfig implements Serializable {

    @TableId(value = "id")
    String id;

    @TableField
    String name;

    @TableField
    String note;

    @TableField
    String path;

    /**
     * application/x-www-form-urlencoded 类API对应的参数
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    String params;

    @TableField
    Integer status;

    @TableField
    Integer access;

    @TableField("group_id")
    String groupId;

    @TableField(value = "content_type")
    String contentType;

    /**
     * application/json 类API对应的json参数示例
     */
    @TableField(value = "json_param", updateStrategy = FieldStrategy.IGNORED)
    String jsonParam;

    @TableField
    String task;

    @TableField(value = "create_time")
    String createTime;

    @TableField(value = "update_time")
    String updateTime;

    @TableField("create_user_id")
    Integer createUserId;


    @TableField(exist = false)
    JSONArray paramsJson;// params的json格式
    @TableField(exist = false)
    JSONArray taskJson;//task的json格式
    @TableField(exist = false)
    List<ApiPluginConfig> alarmPlugins;
    @TableField(exist = false)
    ApiPluginConfig cachePlugin;
    @TableField(exist = false)
    ApiPluginConfig globalTransformPlugin;

}
