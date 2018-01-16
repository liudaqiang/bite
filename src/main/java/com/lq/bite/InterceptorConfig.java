package com.lq.bite;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.lq.bite.interceptor.IsCookieAccountKeysInterceptor;
/**
 * 全局配置拦截器
 * @author l.q
 *
 */
@Configuration
public class InterceptorConfig extends WebMvcConfigurerAdapter {
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 多个拦截器组成一个拦截器链
		// addPathPatterns 用于添加拦截规则
		// excludePathPatterns 用户排除拦截
		registry.addInterceptor(new IsCookieAccountKeysInterceptor()).addPathPatterns("/**")
				.excludePathPatterns("/page/toIndex","/account/save","/account/checkAccount");
		super.addInterceptors(registry);
	}
}
