package com.gitee.freakchicken.dbapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
//@ComponentScan(value = "com.gitee.freakchicken.dbapi", excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {APIFilter.class})})  //filter会自动注册
//@PropertySource("application-gateway.properties")
public class DBApiGateWay {
    public static void main(String[] args) {
        System.setProperty("spring.profiles.active","gateway");

        SpringApplication.run(DBApiGateWay.class, args);
    }

}
