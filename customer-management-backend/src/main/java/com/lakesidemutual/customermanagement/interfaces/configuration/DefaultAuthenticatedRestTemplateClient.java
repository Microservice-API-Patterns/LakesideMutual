package com.lakesidemutual.customermanagement.interfaces.configuration;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

/**
 * Provides a RestTemplate that can be injected into other components. The
 * RestTemplate uses the configured API Key when making a request.
 */
@Configuration
@Profile("default")
public class DefaultAuthenticatedRestTemplateClient {
	@Autowired
	private HeaderRequestInterceptor headerRequestInterceptor;

	@Bean
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setInterceptors(Arrays.asList(headerRequestInterceptor));
		return restTemplate;
	}
}