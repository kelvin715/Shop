package com.neuedu.ws.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.neuedu.ws.common.filter.CorsFilter;

@Configuration
public class FilterConfig {
	@Bean
	public FilterRegistrationBean filterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new CorsFilter());
		List<String> urlList = new ArrayList<String>();
	    urlList.add("/*");
	    registration.setUrlPatterns(urlList);
		registration.setName("UrlFilter");
		registration.setOrder(1);
		return registration;
	}
}

