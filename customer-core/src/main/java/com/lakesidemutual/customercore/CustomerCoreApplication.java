package com.lakesidemutual.customercore;

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
 * CustomerCoreApplication is the execution entry point of the Customer Core which
 * is one of the functional/system/application Bounded Contexts of Lakeside Mutual.
 */
@SpringBootApplication
@EnableSwagger2
public class CustomerCoreApplication implements BoundedContext {
	private static Logger logger = LoggerFactory.getLogger(CustomerCoreApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(CustomerCoreApplication.class, args);
		logger.info("--- Customer Core started ---");
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
		return new ApiInfo("Customer Core API", "This API allows clients to create new customers and retrieve details about existing customers.", "v1.0.0", null, new Contact("", "", ""), null, null, Collections.emptyList());
	}
}
