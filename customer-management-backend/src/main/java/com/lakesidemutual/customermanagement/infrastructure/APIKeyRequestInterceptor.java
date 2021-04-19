package com.lakesidemutual.customermanagement.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * The APIKeyRequestInterceptor class sets the API Key for outgoing requests to the customer-core service.
 * */
@Component
public class APIKeyRequestInterceptor implements RequestInterceptor {
	@Value("${apikey.header}")
	private String apiKeyHeader;

	@Value("${apikey.value}")
	private String apiKeyValue;

	@Override
	public void apply(RequestTemplate template) {
		template.header(apiKeyHeader, "Bearer " + apiKeyValue);
		template.header("Accept", "application/json");
	}
}
