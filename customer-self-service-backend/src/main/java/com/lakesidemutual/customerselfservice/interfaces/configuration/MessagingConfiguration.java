package com.lakesidemutual.customerselfservice.interfaces.configuration;

import java.util.HashMap;
import java.util.Map;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import com.lakesidemutual.customerselfservice.domain.insurancequoterequest.InsuranceQuoteExpiredEvent;
import com.lakesidemutual.customerselfservice.domain.insurancequoterequest.InsuranceQuoteResponseEvent;
import com.lakesidemutual.customerselfservice.domain.insurancequoterequest.PolicyCreatedEvent;

@Configuration
public class MessagingConfiguration {
	@Value("${policymanagement.tcpBrokerBindAddress}")
	private String brokerURL;

	@Value("${spring.activemq.user}")
	private String username;

	@Value("${spring.activemq.password}")
	private String password;

	@Bean
	public JmsTemplate jmsTemplate(){
		JmsTemplate template = new JmsTemplate();
		template.setMessageConverter(jacksonJmsMessageConverter());
		template.setConnectionFactory(connectionFactory());
		return template;
	}

	@Bean
	public ActiveMQConnectionFactory connectionFactory(){
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
		connectionFactory.setBrokerURL(brokerURL);
		connectionFactory.setUserName(username);
		connectionFactory.setPassword(password);
		connectionFactory.setTrustAllPackages(true);
		return connectionFactory;
	}

	@Bean
	public MessageConverter jacksonJmsMessageConverter() {
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		converter.setTargetType(MessageType.TEXT);
		converter.setTypeIdPropertyName("_type");

		final Map<String, Class<?>> typeIdMappings = new HashMap<>();
		typeIdMappings.put("com.lakesidemutual.policymanagement.domain.insurancequoterequest.InsuranceQuoteResponseEvent", InsuranceQuoteResponseEvent.class);
		typeIdMappings.put("com.lakesidemutual.policymanagement.domain.insurancequoterequest.InsuranceQuoteExpiredEvent", InsuranceQuoteExpiredEvent.class);
		typeIdMappings.put("com.lakesidemutual.policymanagement.domain.insurancequoterequest.PolicyCreatedEvent", PolicyCreatedEvent.class);
		converter.setTypeIdMappings(typeIdMappings);
		return converter;
	}

	@Bean
	public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory());
		factory.setConcurrency("1-1");
		factory.setMessageConverter(jacksonJmsMessageConverter());
		return factory;
	}
}