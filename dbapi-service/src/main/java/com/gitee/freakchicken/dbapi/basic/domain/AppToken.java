package com.gitee.freakchicken.dbapi.basic.domain;

import java.io.Serializable;

public class AppToken implements Serializable {
    String appId;
    String token;
    Long expireAt;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(Long expireAt) {
        this.expireAt = expireAt;
    }
}