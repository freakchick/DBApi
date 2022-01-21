package com.gitee.freakchicken.dbapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(value = "com.gitee.freakchicken.dbapi.basic", excludeFilters = {
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = {
                "com.gitee.freakchicken.dbapi.basic.dao.*",
                "com.gitee.freakchicken.dbapi.basic.filter.*",
                "com.gitee.freakchicken.dbapi.basic.service.*",
                "com.gitee.freakchicken.dbapi.basic.servlet.*"
        })
})
//@EnableCaching
//@PropertySource("classpath:application-gateway.yml")
public class DBApiGateWay {
    public static void main(String[] args) {
        System.setProperty("spring.profiles.active", "gateway");

        SpringApplication.run(DBApiGateWay.class, args);
    }

}
