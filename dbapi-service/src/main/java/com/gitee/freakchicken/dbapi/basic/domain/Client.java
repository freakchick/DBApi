package com.gitee.freakchicken.dbapi.basic.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("client")
public class Client implements Serializable {

    @TableId()
    String id;

    @TableField()
    String secret;

    @TableField()
    String name;

    @TableField()
    String note;

    @TableField()
    String expireDesc;

    @TableField()
    Long expireDuration; // -1永久；0 单次失效；> 0 失效时间

    @TableField()
    String token;

    @TableField("expire_at")
    Long expireAt;

}