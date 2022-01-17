package com.gitee.freakchicken.dbapi;

import com.gitee.freakchicken.dbapi.filter.APIFilter;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;


@SpringBootApplication
@ComponentScan(value = "com.gitee.freakchicken.dbapi",
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {APIFilter.class})})  //filter会自动注册
@MapperScan("com.gitee.freakchicken.dbapi.dao")
@EnableCaching
//@PropertySource("application-manager.properties")
@EnableDiscoveryClient
public class DBApiManager {
    public static void main(String[] args) {
        System.setProperty("spring.profiles.active","manager");
        SpringApplication.run(DBApiManager.class, args);
    }

}
