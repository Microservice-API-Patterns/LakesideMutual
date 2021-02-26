package com.lakesidemutual.customerselfservice.infrastructure;

import java.util.Date;

import org.microserviceapipatterns.domaindrivendesign.InfrastructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.lakesidemutual.customerselfservice.domain.insurancequoterequest.CustomerDecisionEvent;
import com.lakesidemutual.customerselfservice.domain.insurancequoterequest.InsuranceQuoteRequestEvent;
import com.lakesidemutual.customerselfservice.interfaces.dtos.insurancequoterequest.InsuranceQuoteRequestDto;

/**
 * PolicyManagementMessageProducer is an infrastructure service class that is used to notify the Policy Management Backend
 * when a new insurance quote request has been created (InsuranceQuoteRequestEvent) or when a customer has accepted or rejected
 * an insurance quote (CustomerDecisionEvent). These events are transmitted via an ActiveMQ message queue.
 * */
@Component
public class PolicyManagementMessageProducer implements InfrastructureService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${insuranceQuoteRequestEvent.queueName}")
	private String insuranceQuoteRequestEventQueue;

	@Value("${customerDecisionEvent.queueName}")
	private String customerDecisionEventQueue;

	@Autowired
	private JmsTemplate jmsTemplate;

	public void sendInsuranceQuoteRequest(Date date, InsuranceQuoteRequestDto insuranceQuoteRequestDto) {
		InsuranceQuoteRequestEvent insuranceQuoteRequestEvent = new InsuranceQuoteRequestEvent(date, insuranceQuoteRequestDto);
		emitInsuranceQuoteRequestEvent(insuranceQuoteRequestEvent);
	}

	public void sendCustomerDecision(Date date, Long insuranceQuoteRequestId, boolean quoteAccepted) {
		CustomerDecisionEvent customerDecisionEvent = new CustomerDecisionEvent(date, insuranceQuoteRequestId, quoteAccepted);
		emitCustomerDecisionEvent(customerDecisionEvent);
	}

	private void emitInsuranceQuoteRequestEvent(InsuranceQuoteRequestEvent insuranceQuoteRequestEvent) {
		try {
			jmsTemplate.convertAndSend(insuranceQuoteRequestEventQueue, insuranceQuoteRequestEvent);
			logger.info("Successfully sent a insurance quote request to the Policy Management backend.");
		} catch(JmsException exception) {
			logger.error("Failed to send a insurance quote request to the Policy Management backend.", exception);
		}
	}

	private void emitCustomerDecisionEvent(CustomerDecisionEvent customerDecisionEvent) {
		try {
			jmsTemplate.convertAndSend(customerDecisionEventQueue, customerDecisionEvent);
			logger.info("Successfully sent a customer decision event to the Policy Management backend.");
		} catch(JmsException exception) {
			logger.error("Failed to send a customer decision event to the Policy Management backend.", exception);
		}
	}
}