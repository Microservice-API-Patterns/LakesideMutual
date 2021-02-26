package com.lakesidemutual.policymanagement;

import org.microserviceapipatterns.domaindrivendesign.BoundedContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

/**
 * PolicyManagementApplication is the execution entry point of the Policy Management backend which
 * is one of the functional/system/application Bounded Contexts of Lakeside Mutual.
 */
@SpringBootApplication
@EnableJms
public class PolicyManagementApplication implements BoundedContext {
	private static Logger logger = LoggerFactory.getLogger(PolicyManagementApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(PolicyManagementApplication.class, args);
		logger.info("--- Policy Management backend started ---");
	}
}
