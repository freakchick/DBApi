package com.gitee.freakchicken.dbapi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@MapperScan("com.gitee.freakchicken.dbapi.basic.dao")
@EnableCaching
@ComponentScan(value = {"com.gitee.freakchicken.dbapi.basic","com.gitee.freakchicken.dbapi.conf"})
@EnableAsync
//@PropertySource("application-standalone.properties")
public class DBApiStandalone {
    public static void main(String[] args) {
        System.setProperty("spring.profiles.active","standalone");
        SpringApplication.run(DBApiStandalone.class, args);
    }
}
