package com.lakesidemutual.customermanagement.interfaces.configuration;

import jakarta.servlet.Filter;

import jakarta.servlet.Servlet;
import org.apache.catalina.servlets.DefaultServlet;
import org.h2.server.web.JakartaWebServlet;
import org.h2.server.web.WebServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * The WebConfiguration class is used to customize the default Spring MVC configuration.
 * */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {
	/**
	 * This web servlet makes the web console of the H2 database engine available at the "/console" endpoint.
	 * */
	@Bean
	public ServletRegistrationBean<JakartaWebServlet> h2servletRegistration() {
		ServletRegistrationBean<JakartaWebServlet> registrationBean = new ServletRegistrationBean<>(new JakartaWebServlet());
		registrationBean.addUrlMappings("/console/*");
		return registrationBean;
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