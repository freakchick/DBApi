package com.gitee.freakchicken.dbapi.basic.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class ClientToken implements Serializable {

    String clientId;
    String token;
    Long expireAt;
    
}