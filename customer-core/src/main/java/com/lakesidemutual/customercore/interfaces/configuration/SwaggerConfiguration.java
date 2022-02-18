package com.lakesidemutual.customercore.interfaces.configuration;

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
				.info(new Info().title("Customer Core API")
						.description("This API allows clients to create new customers and retrieve details about existing customers.")
						.version("v1.0.0")
						.license(new License().name("Apache 2.0")));
	}
}
