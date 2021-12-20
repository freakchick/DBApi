package com.gitee.freakchicken.dbapi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

/**
 * @program: dbApi
 * @description:
 * @author: jiangqiang
 * @create: 2021-01-19 16:31
 **/
//@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
@SpringBootApplication
@ServletComponentScan
@MapperScan("com.gitee.freakchicken.dbapi.dao")
@EnableCaching
@PropertySource("application-core.properties")
//@EnableDiscoveryClient
public class DBApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(DBApiApplication.class, args);
    }

}
