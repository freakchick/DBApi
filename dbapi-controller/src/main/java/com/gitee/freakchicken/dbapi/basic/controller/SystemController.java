package com.gitee.freakchicken.dbapi.basic.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/system")
public class SystemController {
    @Value("${version}")
    String version;

    @Value("${dbapi.mode}")
    String mode;

    @RequestMapping("/version")
    public String getVersion() {
        return version;
    }

    @RequestMapping("/mode")
    public String mode() {
        String docker = System.getenv("DOCKER");
        if ("true".equals(docker)) {
            return mode + " in docker";
        } else {
            return mode;
        }
    }
}
