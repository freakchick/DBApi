package com.jq.dbapi.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "group")
public class ApiAuth {

    @TableId(value = "id", type = IdType.AUTO)
    Integer id;

    @TableField("token_id")
    Integer tokenId;

    @TableField("group_id")
    Integer groupId;
}
