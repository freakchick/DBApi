package com.gitee.freakchicken.dbapi.gateway;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(value = {"com.gitee.freakchicken.dbapi.basic", "com.gitee.freakchicken.dbapi.gateway"},
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = {
                        "com.gitee.freakchicken.dbapi.basic.filter.*", //filter会自动注册，要排除
                        "com.gitee.freakchicken.dbapi.basic.controller.*", //安装包启动的时候排除 controller.jar
                        "com.gitee.freakchicken.dbapi.basic.conf.*", //安装包启动的时候排除 controller.jar
                        "com.gitee.freakchicken.dbapi.basic.service.LoadPluginOnSpringReady"
                })
        })
@MapperScan("com.gitee.freakchicken.dbapi.basic.dao")
@EnableCaching
//@PropertySource("classpath:application-gateway.yml")
public class DBApiGateWay {
    public static void main(String[] args) {
        System.setProperty("spring.profiles.active", "gateway");
        System.setProperty("reactor.netty.http.server.accessLogEnabled","true");
        SpringApplication.run(DBApiGateWay.class, args);
    }

}
