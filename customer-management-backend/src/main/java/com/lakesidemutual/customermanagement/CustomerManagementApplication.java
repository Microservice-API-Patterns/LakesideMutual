package com.lakesidemutual.customermanagement;

import org.microserviceapipatterns.domaindrivendesign.BoundedContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * CustomerManagementApplication is the execution entry point of the Customer Management backend which
 * is one of the functional/system/application Bounded Contexts of Lakeside Mutual.
 */
@SpringBootApplication
@EnableFeignClients
public class CustomerManagementApplication implements BoundedContext {
	private static Logger logger = LoggerFactory.getLogger(CustomerManagementApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(CustomerManagementApplication.class, args);
		logger.info("--- Customer Management backend started ---");
	}
}
