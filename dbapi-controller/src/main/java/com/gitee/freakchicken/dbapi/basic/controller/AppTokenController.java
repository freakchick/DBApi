package com.gitee.freakchicken.dbapi.basic.controller;

import com.gitee.freakchicken.dbapi.basic.domain.AppToken;
import com.gitee.freakchicken.dbapi.basic.service.AppTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token")
public class AppTokenController {

    @Autowired
    private AppTokenService tokenService;

    /**
     * 客户端获取token
     *
     * @param appId
     * @param secret
     * @return
     */
    @PostMapping("/generate")
    public AppToken getToken(String appId, String secret) {
        AppToken token = tokenService.generateToken(appId, secret);
        return token;
    }

}