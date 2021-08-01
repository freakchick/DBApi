package com.gitee.freakchicken.dbapi.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "token")
public class Token {

    @TableId(value = "id", type = IdType.AUTO)
    Integer id;

    @TableField
    String token;

    @TableField
    Long expire;

    @TableField
    String note;

    @TableField("create_time")
    Long createTime;

}
