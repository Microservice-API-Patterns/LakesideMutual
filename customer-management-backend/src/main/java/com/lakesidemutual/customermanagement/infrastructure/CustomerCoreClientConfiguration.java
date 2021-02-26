package com.lakesidemutual.customermanagement.infrastructure;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.RequestInterceptor;

/**
 * The CustomerCoreClientConfiguration class configures the request interceptors for the CustomerCoreClient.
 * */
@Configuration
public class CustomerCoreClientConfiguration {
	@Bean
	public Collection<RequestInterceptor> interceptors() {
		return Arrays.asList(new APIKeyRequestInterceptor());
	}
}
