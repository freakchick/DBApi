package com.gitee.freakchicken.dbapi.basic.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("app_info")
public class AppInfo {

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
    long expireTime; // -1永久；0 单次失效；> 0 失效时间

    public String getNote() {
        return note;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getExpireDesc() {
        return expireDesc;
    }

    public void setExpireDesc(String expireDesc) {
        this.expireDesc = expireDesc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }
}