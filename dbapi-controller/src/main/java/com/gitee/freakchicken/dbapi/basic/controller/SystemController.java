package com.gitee.freakchicken.dbapi.basic.controller;

import com.gitee.freakchicken.dbapi.basic.service.NacosService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@RequestMapping("/system")
public class SystemController {
    @Value("${version}")
    String version;

    @Value("${dbapi.mode}")
    String mode;

    @Autowired
    NacosService nacosService;

    @Value("${dbapi.api.context}")
    String apiContext;

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

    @RequestMapping("/getIPPort")
    public String getIPPort(HttpServletRequest request) {

        if ("standalone".equals(mode)) {
            return request.getServerName() + ":" + request.getServerPort() + "/" + apiContext;
        } else if ("cluster".equals(mode)) {
            return nacosService.getGatewayAddress() + "/" + apiContext;
        } else {
            return null;
        }
    }

    @RequestMapping("/getIP")
    public String getIP(HttpServletRequest request) {

        if ("standalone".equals(mode)) {
            return request.getServerName() + ":" + request.getServerPort();
        } else if ("cluster".equals(mode)) {
            return nacosService.getGatewayAddress();
        } else {
            return null;
        }
    }
}
