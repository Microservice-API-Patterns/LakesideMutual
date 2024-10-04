package com.lakesidemutual.customercore.interfaces.configuration;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * The WebSecurityConfiguration class configures the security policies used for the exposed HTTP resource API.
 * In this case, it ensures that only clients with a valid API key can access the API.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration {
	private static final String[] AUTH_WHITELIST = {
			// -- swagger ui
			"/swagger-ui.html",
			"/swagger-ui/**",
			"/v3/api-docs/**",
			"/webjars/**",
			// spring-boot-starter-actuator health checks and other info
			"/actuator/**",
			"/actuator",
			// H2 Console
			"/console/**",
			// Spring Web Services
			"/ws/**",
			"/ws",
			// Thymeleaf demo FE
			"/customercorefe",
	};

	@Value("${apikey.header}")
	private String apiKeyHeader;

	@Value("${apikey.validkeys}")
	private String apiKeyValidKeys;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		final List<String> validAPIKeys = Arrays.asList(apiKeyValidKeys.split(";"));
		final APIKeyAuthFilter filter = new APIKeyAuthFilter(apiKeyHeader);
		filter.setAuthenticationManager(new APIKeyAuthenticationManager(validAPIKeys));

		httpSecurity
				.csrf(AbstractHttpConfigurer::disable)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilter(filter)
				.authorizeHttpRequests(authz -> authz
						.requestMatchers(AUTH_WHITELIST).permitAll()
						.anyRequest().authenticated()
				)
				.headers(headers -> {
					headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable);
					headers.cacheControl(HeadersConfigurer.CacheControlConfig::disable);
				});

		return httpSecurity.build();
	}
}
