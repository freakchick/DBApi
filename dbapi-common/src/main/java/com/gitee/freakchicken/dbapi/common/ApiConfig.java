package com.gitee.freakchicken.dbapi.common;

import java.io.Serializable;

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
    Integer previlege;

    @TableField("group_id")
    String groupId;

    @TableField(value = "content_type")
    String contentType;

    /**
     * application/json 类API对应的json参数示例
     */
    @TableField(value = "json_param", updateStrategy = FieldStrategy.IGNORED)
    String jsonParam;

    @TableField("task_type")
    String taskType;

    @TableField
    String task;

    @TableField(value = "create_time")
    String createTime;

    @TableField(value = "update_time")
    String updateTime;


}
