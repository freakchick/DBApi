package com.gitee.freakchicken.dbapi.basic.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "user")
public class User implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    Integer id;

    @TableField
    String username;

    @TableField
    String password;

    @TableField(exist = false)
    String token;
}
