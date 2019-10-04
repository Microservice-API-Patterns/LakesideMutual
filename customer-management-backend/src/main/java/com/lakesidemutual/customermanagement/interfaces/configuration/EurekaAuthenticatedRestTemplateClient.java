package com.lakesidemutual.customermanagement.interfaces.configuration;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

/**
 * Provides a RestTemplate that can be injected into other components. The
 * RestTemplate uses the configured API Key when making a request.
 */
@Configuration
@Profile("eureka")
public class EurekaAuthenticatedRestTemplateClient {
	@Autowired
	private HeaderRequestInterceptor headerRequestInterceptor;

	@Bean
	/**
	 * The @LoadBalanced annotation configures the RestTemplate to look up the
	 * target URL of a call via Eureka. It is required even if there is no real
	 * load-balancing involved.
	 */
	@LoadBalanced
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setInterceptors(Arrays.asList(headerRequestInterceptor));
		return restTemplate;
	}
}