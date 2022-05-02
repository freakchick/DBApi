package com.gitee.freakchicken.dbapi.apiserver.conf;

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
    String apiContext;

    @Autowired
    private ApiAuthFilter apiAuthFilter;

    @Bean
    public FilterRegistrationBean authFilter() {
        int authFilterOrder = 2;
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
