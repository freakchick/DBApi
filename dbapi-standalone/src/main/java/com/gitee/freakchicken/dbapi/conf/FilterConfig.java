package com.gitee.freakchicken.dbapi.conf;

import com.gitee.freakchicken.dbapi.basic.filter.ApiAuthFilter;
import com.gitee.freakchicken.dbapi.basic.filter.ApiHeaderFilter;
import com.gitee.freakchicken.dbapi.basic.filter.ApiIPFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: 用于注册Filter
 * @program: dbApi
 * @author: kensan
 * @create: 2022-04-16 12:43
 */
@Slf4j
@Configuration
public class FilterConfig {
    @Value("${dbapi.api.context}")
    private String apiContext;

    @Autowired
    private ApiIPFilter apiIPFilter;

    @Autowired
    private ApiAuthFilter apiAuthFilter;


    @Bean
    public FilterRegistrationBean apiHeaderFilter() {
        // issues/I51LOI
        int apiHeaderFilterOrder = 1;
        String format = String.format("/%s/*", apiContext);
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new ApiHeaderFilter());
        registrationBean.addUrlPatterns(format);
        registrationBean.setOrder(apiHeaderFilterOrder);
        registrationBean.setEnabled(true);
        log.info("regist apiHeaderFilter for {} UrlPatterns, and order is {}",format,apiHeaderFilterOrder);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean ipFilter() {
        int ipfilterOrder = 2;
        String format = String.format("/%s/*", apiContext);
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(apiIPFilter);
        registrationBean.addUrlPatterns(format);
        registrationBean.setOrder(ipfilterOrder);
        registrationBean.setEnabled(true);
        log.info("regist ipFilter for {} UrlPatterns, and order is {}",format,ipfilterOrder);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean authFilter() {
        int authFilterOrder = 3;
        String format = String.format("/%s/*", apiContext);
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(apiAuthFilter);
        registrationBean.addUrlPatterns(format);
        registrationBean.setOrder(authFilterOrder);
        registrationBean.setEnabled(true);
        log.info("regist authFilter for {} UrlPatterns, and order is {}",format,authFilterOrder);
        return registrationBean;
    }
}
