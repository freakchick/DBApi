package com.gitee.freakchicken.dbapi;

import com.gitee.freakchicken.dbapi.basic.servlet.APIServlet;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@MapperScan("com.gitee.freakchicken.dbapi.basic.dao")
@EnableCaching
@ComponentScan(value = "com.gitee.freakchicken.dbapi.basic", excludeFilters = {
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = {
                "com.gitee.freakchicken.dbapi.basic.filter.*",
                "com.gitee.freakchicken.dbapi.basic.conf.*",
                "com.gitee.freakchicken.dbapi.basic.controller.*"
        })
}) //安装包启动的时候排除controller相关
//@PropertySource("application-apiServer.properties")
public class DBApiApiServer {
    public static void main(String[] args) {
        System.setProperty("spring.profiles.active","apiServer");
        SpringApplication.run(DBApiApiServer.class, args);
    }

    @Autowired
    APIServlet APIServlet;


    @Bean
    public ServletRegistrationBean clusterApiServlet() {
        ServletRegistrationBean bean = new ServletRegistrationBean(APIServlet);
        bean.addUrlMappings("/api/*");
        return bean;
    }

}
