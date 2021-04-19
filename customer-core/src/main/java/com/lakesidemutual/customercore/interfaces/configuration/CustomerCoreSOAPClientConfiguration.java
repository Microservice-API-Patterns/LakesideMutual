package com.lakesidemutual.customercore.interfaces.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class CustomerCoreSOAPClientConfiguration {

	@Bean
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		// this package must match the package in the <generatePackage> specified in
		// pom.xml
		marshaller.setContextPath("com.lm.ccore");
		return marshaller;
	}

	@Bean
	public CustomerCoreRemoteSOAPProxy customerWSClient(Jaxb2Marshaller marshaller) {
		CustomerCoreRemoteSOAPProxy client = new CustomerCoreRemoteSOAPProxy();
		client.setDefaultUri("http://localhost:8110/ws");
		client.setMarshaller(marshaller);
		client.setUnmarshaller(marshaller);
		return client;
	}
}