package com.gitee.freakchicken.dbapi.conf;

import com.gitee.freakchicken.dbapi.basic.servlet.APIServlet;
import com.gitee.freakchicken.dbapi.basic.servlet.TokenServlet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @program: dbApi
 * @author: kensan
 * @create: 2022-04-16 12:45
 */
@Slf4j
@Configuration
public class ServletConfig {
    @Value("${dbapi.api.context}")
    private String apiContext;

    @Autowired
    private APIServlet apiServlet;

    @Autowired
    private TokenServlet tokenServlet;

    @Bean
    public ServletRegistrationBean getServletRegistrationBean() {
        String format = String.format("/%s/*", apiContext);
        ServletRegistrationBean bean = new ServletRegistrationBean(apiServlet);
        bean.addUrlMappings(format);
        log.info("regist APIServlet servelet for {} urlMappings",format);
        return bean;
    }

    @Bean
    public ServletRegistrationBean tokenServletRegistrationBean() {
        ServletRegistrationBean bean = new ServletRegistrationBean(tokenServlet);
        bean.addUrlMappings("/token/generate");
        log.info("regist tokenServlet servelet ");
        return bean;
    }
}
