package com.gitee.freakchicken.dbapi;

import com.gitee.freakchicken.dbapi.filter.APIFilter;
import com.gitee.freakchicken.dbapi.servlet.APIServlet;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@ComponentScan(value = "com.gitee.freakchicken.dbapi",
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {APIFilter.class})})  //filter会自动注册
@MapperScan("com.gitee.freakchicken.dbapi.dao")
@EnableCaching
//@PropertySource("application-apiServer.properties")
//@EnableDiscoveryClient
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
