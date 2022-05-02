package com.gitee.freakchicken.dbapi;

import com.gitee.freakchicken.dbapi.basic.filter.ApiAuthFilter;
import com.gitee.freakchicken.dbapi.basic.filter.ApiIPFilter;
import com.gitee.freakchicken.dbapi.basic.servlet.APIServlet;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
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
