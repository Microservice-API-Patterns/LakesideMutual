package com.lakesidemutual.customercore.interfaces.configuration;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * The WebSecurityConfiguration class configures the security policies used for the exposed HTTP resource API.
 * In this case, it ensures that only clients with a valid API key can access the API.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
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

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		final List<String> validAPIKeys = Arrays.asList(apiKeyValidKeys.split(";"));
		final APIKeyAuthFilter filter = new APIKeyAuthFilter(apiKeyHeader);
		filter.setAuthenticationManager(new APIKeyAuthenticationManager(validAPIKeys));

		httpSecurity.headers().frameOptions().disable().and().csrf().disable().exceptionHandling()
		.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
		.addFilter(filter).authorizeRequests().antMatchers(AUTH_WHITELIST).permitAll().anyRequest().authenticated();

		// Disable Cache-Control for Conditional Requests
		httpSecurity.headers().cacheControl().disable();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().mvcMatchers(HttpMethod.OPTIONS, "/**");
		web.ignoring().mvcMatchers("/swagger-ui.html/**", "/configuration/**", "/swagger-resources/**", "/v2/api-docs",
				"/webjars/**");
	}

	
}
