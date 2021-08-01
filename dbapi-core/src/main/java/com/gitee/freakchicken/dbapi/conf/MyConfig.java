package com.gitee.freakchicken.dbapi.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

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

		List<String > patterns = new ArrayList<>();
		patterns.add("/user/login");
		patterns.add("/api/**");
		patterns.add("/js/**");
		patterns.add("/css/**");
		patterns.add("/fonts/**");
		patterns.add("/index.html");

		registry.addInterceptor(jwtAuthenticationInterceptor).addPathPatterns("/**")
				.excludePathPatterns(patterns);
	}


}