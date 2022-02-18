package com.lakesidemutual.customermanagement.interfaces.configuration;

import java.util.Collections;
import java.util.function.Predicate;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The SwaggerConfiguration class configures the HTTP resource API documentation.
 */
@Configuration
public class SwaggerConfiguration {

	@Bean
	public OpenAPI customerSelfServiceApi() {
		return new OpenAPI()
				.info(new Info().title("Customer Management API")
						.description("This API allows call center operators to interact with customers and to edit their user profiles.")
						.version("v1.0.0")
						.license(new License().name("Apache 2.0")));
	}
}
