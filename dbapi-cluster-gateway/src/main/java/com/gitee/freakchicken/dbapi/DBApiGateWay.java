package com.gitee.freakchicken.dbapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(value = "com.gitee.freakchicken.dbapi", excludeFilters = {
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = {
                "com.gitee.freakchicken.dbapi.dao.*",
                "com.gitee.freakchicken.dbapi.filter.*",
                "com.gitee.freakchicken.dbapi.service.*",
                "com.gitee.freakchicken.dbapi.servlet.*"
        })
})
//@PropertySource("classpath:application-gateway.yml")
public class DBApiGateWay {
    public static void main(String[] args) {
        System.setProperty("spring.profiles.active", "gateway");

        SpringApplication.run(DBApiGateWay.class, args);
    }

}
