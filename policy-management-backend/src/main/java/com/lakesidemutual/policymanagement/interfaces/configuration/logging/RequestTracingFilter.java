package com.lakesidemutual.policymanagement.interfaces.configuration.logging;

import java.io.IOException;
import java.util.Random;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * This RequestTracingFilter class generates a new random ID for each request and stores
 * it in the MDC (Mapped Diagnostic Context). The ID will then appear in each log entry
 * that is logged as a result of the corresponding request. This can help correlate different
 * log entries during debugging.
 * */
@Component
@Order(1)
public class RequestTracingFilter implements Filter {
	private final static String REQUEST_ID_KEY = "requestId";
	private final Random rand = new Random();

	private String createRequestId() {
		return Integer.toString(rand.nextInt(9999));
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		MDC.put(REQUEST_ID_KEY, createRequestId());
		chain.doFilter(request, response);
	}
}
