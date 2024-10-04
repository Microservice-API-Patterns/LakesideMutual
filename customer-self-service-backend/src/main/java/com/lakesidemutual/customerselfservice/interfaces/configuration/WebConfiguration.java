package com.lakesidemutual.customerselfservice.interfaces.configuration;

import jakarta.servlet.Filter;

import org.h2.server.web.WebServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * The WebConfiguration class is used to customize the default Spring MVC configuration.
 * */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

	@Autowired
	HandlerInterceptor rateLimitInterceptor;

	/**
	 * This method adds a custom interceptor to the InterceptorRegistry in order to enable rate-limiting.
	 * */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(rateLimitInterceptor);
	}

	/**
	 * This is a filter that generates an ETag value based on the content of the response. This ETag is compared to the If-None-Match header
	 * of the request. If these headers are equal, the response content is not sent, but rather a 304 "Not Modified" status instead.
	 * */
	@Bean
	public Filter shallowETagHeaderFilter() {
		return new ShallowEtagHeaderFilter();
	}
}