package com.jq.dbapi.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 添加拦截
 * @author jiangqiang
 * @date 2019年4月14日上午12:10:36
 */
@Configuration
public class MyConfig implements WebMvcConfigurer {

	@Autowired
	private ApiInterceptor apiInterceptor;
	@Autowired
	JwtAuthenticationInterceptor jwtAuthenticationInterceptor;

	/**
	 * 添加拦截器
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(apiInterceptor).addPathPatterns("/api/**");
		registry.addInterceptor(jwtAuthenticationInterceptor).addPathPatterns("/**").excludePathPatterns("/api/**").excludePathPatterns("/user/login");
	}


}