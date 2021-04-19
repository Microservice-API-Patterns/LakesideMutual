package com.lakesidemutual.customermanagement.interfaces.configuration;

import java.io.IOException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

@Component
public class HeaderRequestInterceptor implements ClientHttpRequestInterceptor {
	@Value("${apikey.header}")
	private String apiKeyHeader;

	@Value("${apikey.value}")
	private String apiKeyValue;

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		final HttpHeaders httpHeaders = request.getHeaders();
		httpHeaders.set(apiKeyHeader, "Bearer " + apiKeyValue);
		httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		return execution.execute(request, body);
	}
}