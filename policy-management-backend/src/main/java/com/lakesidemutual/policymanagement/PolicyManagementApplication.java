package com.lakesidemutual.policymanagement;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;

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
 * PolicyManagementApplication is the execution entry point of the Policy Management backend which
 * is one of the functional/system/application Bounded Contexts of Lakeside Mutual.
 */
@SpringBootApplication
@EnableSwagger2
@EnableJms
public class PolicyManagementApplication implements BoundedContext {
	private static Logger logger = LoggerFactory.getLogger(PolicyManagementApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(PolicyManagementApplication.class, args);
		logger.info("--- Policy Management backend started ---");
	}

	@Bean
	public Docket policyManagementApp() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.any())
				.paths(paths())
				.build().apiInfo(apiInfo());
	}

	private Predicate<String> paths() {
		return Predicates.not(PathSelectors.regex("/error|/actuator.*"));
	}

	private ApiInfo apiInfo() {
		// agent application NYI (could demo Spring Web Services, WSDL/SOAP)
		return new ApiInfo("PolicyManagement API", "This API allows LM staff to manage the policies of their customers.", "v1.0.0", null, new Contact("", "", ""), null, null, Collections.emptyList());
	}
}
