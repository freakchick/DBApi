package com.gitee.freakchicken.dbapi.apiserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@MapperScan("com.gitee.freakchicken.dbapi.basic.dao")
@EnableCaching
@EnableAsync
@ComponentScan(value = {"com.gitee.freakchicken.dbapi.basic", "com.gitee.freakchicken.dbapi.apiserver"},
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = {
                        "com.gitee.freakchicken.dbapi.basic.filter.ApiIPFilter", //filter会自动注册
                        "com.gitee.freakchicken.dbapi.basic.conf.*",//安装包启动的时候排除controller相关
                        "com.gitee.freakchicken.dbapi.basic.controller.*"  //安装包启动的时候排除controller相关
                })
        })
//@PropertySource("application-apiServer.properties")
public class DBApiApiServer {
    public static void main(String[] args) {
        System.setProperty("spring.profiles.active", "apiServer");
        SpringApplication.run(DBApiApiServer.class, args);
    }
}
