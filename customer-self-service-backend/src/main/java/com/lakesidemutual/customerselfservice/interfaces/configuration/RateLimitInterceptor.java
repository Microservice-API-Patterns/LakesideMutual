package com.lakesidemutual.customerselfservice.interfaces.configuration;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import es.moki.ratelimitj.core.limiter.request.RequestLimitRule;
import es.moki.ratelimitj.core.limiter.request.RequestRateLimiter;
import es.moki.ratelimitj.inmemory.request.InMemorySlidingWindowRequestRateLimiter;

/**
 * RateLimitInterceptor ensures that client can't overuse the service. What
 * follows is a simple implementation that limits clients to 60 requests per
 * minute. Clients are identified by their IP address. Once the limit is
 * reached, a {@code 429 Too Many Requests} response is returned.
 *
 * To test the limit, the Apache Bench tool ab can be used:
 *
 * <pre>
 * {@code
 *
 * ab -n 100  http://localhost:8080/customers
 *
 * }
 * </pre>
 *
 * In the output you should see that all 100 requests were answered but 40 had a
 * Non-2xx response:
 *
 * <pre>
 * {@code
 *
 * Complete requests:      100
 * Failed requests:        40
 * Non-2xx responses:      40
 *
 * }
 * </pre>
 *
 * Note that the Rate Limiting library uses an in-memory store for the rate
 * limits, so you can simply restart the server to reset the limit.
 *
 * This interceptor is configured in {@link WebConfiguration} to intercept all
 * requests to this application.
 *
 * @see <a href=
 *      "https://www.microservice-api-patterns.org/patterns/quality/qualityManagementAndGovernance/RateLimit">https://www.microservice-api-patterns.org/patterns/quality/qualityManagementAndGovernance/RateLimit</a>
 */
@Component
public class RateLimitInterceptor implements HandlerInterceptor {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private RequestRateLimiter requestRateLimiter;

	private int requestsPerMinute;

	@Autowired
	public RateLimitInterceptor(@Value("${rate.limit.perMinute}") int requestsPerMinute) {
		this.requestsPerMinute = requestsPerMinute;
		Set<RequestLimitRule> rules = Collections
				.singleton(RequestLimitRule.of(Duration.of( 1, ChronoUnit.MINUTES), requestsPerMinute));
		requestRateLimiter = new InMemorySlidingWindowRequestRateLimiter(rules);
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {

		String clientRemoteAddr = request.getRemoteAddr();

		boolean overLimit = requestRateLimiter.overLimitWhenIncremented(clientRemoteAddr);

		if (overLimit) {
			logger.info("Client " + clientRemoteAddr + " has been rate limited.");
			response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
		}
		response.addHeader("X-RateLimit-Limit", String.valueOf(requestsPerMinute));
		return !overLimit;
	}

}