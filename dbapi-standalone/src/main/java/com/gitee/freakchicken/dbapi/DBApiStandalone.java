package com.gitee.freakchicken.dbapi;

import com.gitee.freakchicken.dbapi.basic.filter.APIFilter;
import com.gitee.freakchicken.dbapi.basic.servlet.APIServlet;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;

import java.util.ArrayList;


@SpringBootApplication
@MapperScan("com.gitee.freakchicken.dbapi.basic.dao")
@EnableCaching
@ComponentScan(value = "com.gitee.freakchicken.dbapi.basic")
//@PropertySource("application-standalone.properties")
public class DBApiStandalone {
    public static void main(String[] args) {
        System.setProperty("spring.profiles.active","standalone");
        SpringApplication.run(DBApiStandalone.class, args);
    }

    @Autowired
    APIServlet APIServlet;

    //filter 会自动全局注册
    @Autowired
    APIFilter apiFilter;

    @Bean
    public ServletRegistrationBean getServletRegistrationBean() {
        ServletRegistrationBean bean = new ServletRegistrationBean(APIServlet);
        bean.addUrlMappings("/api/*");
        return bean;
    }

    @Bean
    public FilterRegistrationBean getFilter() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(apiFilter);
        ArrayList<String> urls = new ArrayList<>();
        urls.add("/api/*");//配置过滤规则
        registrationBean.setUrlPatterns(urls);
//        registrationBean.setOrder(3);
        return registrationBean;
    }
}
