package com.lakesidemutual.policymanagement.infrastructure;

import org.microserviceapipatterns.domaindrivendesign.InfrastructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.lakesidemutual.policymanagement.domain.insurancequoterequest.InsuranceQuoteExpiredEvent;
import com.lakesidemutual.policymanagement.domain.insurancequoterequest.InsuranceQuoteResponseEvent;
import com.lakesidemutual.policymanagement.domain.insurancequoterequest.PolicyCreatedEvent;

/**
 * CustomerSelfServiceMessageProducer is an infrastructure service class that is used to notify the Customer Self-Service Backend
 * when Lakeside Mutual has responded to a customer's insurance quote request (InsuranceQuoteResponseEvent) or when an insurance quote
 * has expired (InsuranceQuoteExpiredEvent). These events are transmitted via an ActiveMQ message queue.
 * */
@Component
public class CustomerSelfServiceMessageProducer implements InfrastructureService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${insuranceQuoteResponseEvent.queueName}")
	private String quoteResponseQueue;

	@Value("${insuranceQuoteExpiredEvent.queueName}")
	private String quoteExpiredQueue;

	@Value("${policyCreatedEvent.queueName}")
	private String policyCreatedQueue;

	@Autowired
	private JmsTemplate jmsTemplate;

	public void sendInsuranceQuoteResponseEvent(InsuranceQuoteResponseEvent event) {
		try {
			jmsTemplate.convertAndSend(quoteResponseQueue, event);
			logger.info("Successfully sent an insurance quote response to the Customer Self-Service backend.");
		} catch(JmsException exception) {
			logger.error("Failed to send an insurance quote response to the Customer Self-Service backend.", exception);
		}
	}

	public void sendInsuranceQuoteExpiredEvent(InsuranceQuoteExpiredEvent event) {
		try {
			jmsTemplate.convertAndSend(quoteExpiredQueue, event);
			logger.info("Successfully sent an insurance quote expired event to the Customer Self-Service backend.");
		} catch(JmsException exception) {
			logger.error("Failed to send an insurance quote expired event to the Customer Self-Service backend.", exception);
		}
	}

	public void sendPolicyCreatedEvent(PolicyCreatedEvent event) {
		try {
			jmsTemplate.convertAndSend(policyCreatedQueue, event);
			logger.info("Successfully sent an policy created event to the Customer Self-Service backend.");
		} catch(JmsException exception) {
			logger.error("Failed to send an policy created event to the Customer Self-Service backend.", exception);
		}
	}
}