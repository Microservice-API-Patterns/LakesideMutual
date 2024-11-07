package com.lakesidemutual.customercore.interfaces.configuration;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * The WebSecurityConfiguration class configures the security policies used for the exposed HTTP resource API.
 * In this case, it ensures that only clients with a valid API key can access the API.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
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
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        final List<String> validAPIKeys = Arrays.asList(apiKeyValidKeys.split(";"));
        final APIKeyAuthFilter filter = new APIKeyAuthFilter(apiKeyHeader);
        filter.setAuthenticationManager(new APIKeyAuthenticationManager(validAPIKeys));

        http
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
                        .cacheControl(HeadersConfigurer.CacheControlConfig::disable)
                )
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((request, response, exception) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"))
                        .accessDeniedHandler((request, response, exception) -> response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden"))
                )
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(STATELESS)
                )
                .addFilter(filter)
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
