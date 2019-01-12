package com.lakesidemutual.customerselfservice;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.microserviceapipatterns.domaindrivendesign.BoundedContext;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * CustomerSelfServiceApplication is the execution entry point of the Customer Self-Service backend which
 * is one of the functional/system/application Bounded Contexts of Lakeside Mutual.
 */
@SpringBootApplication
@EnableSwagger2
public class CustomerSelfServiceApplication implements BoundedContext {
	private static Logger logger = LoggerFactory.getLogger(CustomerSelfServiceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(CustomerSelfServiceApplication.class, args);
		logger.info("--- Customer Self-Service backend started ---");
	}

	@Bean
	public Docket customerSelfServiceApi() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.any())
				.paths(paths())
				.build().apiInfo(apiInfo());
	}

	private Predicate<String> paths() {
		return Predicates.not(PathSelectors.regex("/error|/actuator.*"));
	}

	private ApiInfo apiInfo() {
		return new ApiInfo("Customer Self-Service API", "This API allows customers of Lakeside Mutual to sign up, log in and manage their policies and user profile themselves.", "v1.0.0", null, new Contact("", "", ""), null, null, Collections.emptyList());
	}
}
