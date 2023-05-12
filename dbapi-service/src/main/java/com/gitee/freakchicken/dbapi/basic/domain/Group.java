package com.gitee.freakchicken.dbapi.basic.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "api_group")
public class Group implements Serializable {

    @TableId(value = "id")
    String id;

    @TableField
    String name;

    @TableField("create_user_id")
    Integer createUserId;

    @TableField(value = "create_time")
    String createTime;

    @TableField(value = "update_time")
    String updateTime;
}
