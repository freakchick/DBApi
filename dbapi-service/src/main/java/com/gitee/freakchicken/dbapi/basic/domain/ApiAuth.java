package com.gitee.freakchicken.dbapi.basic.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "api_auth")
public class ApiAuth {

    @TableId(value = "id", type = IdType.AUTO)
    Integer id;

    @TableField("app_id")
    String appId;

    @TableField("group_id")
    String groupId;
}
