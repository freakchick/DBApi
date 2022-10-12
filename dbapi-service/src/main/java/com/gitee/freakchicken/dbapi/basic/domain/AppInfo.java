package com.gitee.freakchicken.dbapi.basic.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

@TableName("app_info")
public class AppInfo implements Serializable {

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
    long expireDuration; // -1永久；0 单次失效；> 0 失效时间

    @TableField()
    String token;

    @TableField("expire_at")
    long expireAt;

    public String getNote() {
        return note;
    }

    public long getExpireDuration() {
        return expireDuration;
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

    public void setExpireDuration(long expireDuration) {
        this.expireDuration = expireDuration;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(long expireAt) {
        this.expireAt = expireAt;
    }
}