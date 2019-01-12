package com.lakesidemutual.policymanagement.infrastructure;

import java.util.Date;

import org.microserviceapipatterns.domaindrivendesign.InfrastructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.lakesidemutual.policymanagement.domain.policy.PolicyEvent;
import com.lakesidemutual.policymanagement.interfaces.dtos.customer.CustomerDto;
import com.lakesidemutual.policymanagement.interfaces.dtos.policy.PolicyDto;

/**
 * RiskManagementService is an infrastructure service class that is used to notify the Risk Management Server
 * about policy events (e.g., a new policy is created). These events are transmitted via an ActiveMQ message queue.
 * */
@Component
public class RiskManagementService implements InfrastructureService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${riskmanagement.queueName}")
	private String queueName;

	/**
	 * JmsTemplate is a helper class that makes it easy to synchronously access a message queue. Note that this application
	 * uses an ActiveMQ message queue and the corresponding broker service is configured in the MessagingConfiguration
	 * class.
	 *
	 * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/jms/core/JmsTemplate.html">JmsTemplate</a>
	 * @see <a href="https://docs.spring.io/spring/docs/3.1.x/spring-framework-reference/html/jms.html">JMS (Java Messaging Service)</a>
	 * */
	@Autowired
	private JmsTemplate jmsTemplate;

	public void notifyRiskManagement(String originator, Date date, CustomerDto customer, PolicyDto newPolicyDto) {
		final PolicyEvent event = new PolicyEvent(originator, date, customer, newPolicyDto);
		emitEvent(event);
	}

	/**
	 * This method first converts the PolicyEvent into a JSON payload using the MappingJackson2MessageConverter that was set up in the
	 * MessagingConfiguration class. It then sends this payload to the ActiveMQ queue with the given queue name.
	 */
	private void emitEvent(PolicyEvent event) {
		try {
			jmsTemplate.convertAndSend(queueName, event);
			logger.info("Successfully sent a policy event to the risk management message queue.");
		} catch(JmsException exception) {
			logger.error("Failed to send a policy event to the risk management message queue.", exception);
		}
	}
}

