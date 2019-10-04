package com.lakesidemutual.customerselfservice.interfaces;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.lakesidemutual.customerselfservice.domain.insurancequoterequest.InsuranceQuoteRequestAggregateRoot;
import com.lakesidemutual.customerselfservice.domain.insurancequoterequest.PolicyCreatedEvent;
import com.lakesidemutual.customerselfservice.infrastructure.InsuranceQuoteRequestRepository;

/**
 * PolicyCreatedMessageConsumer is a Spring component that consumes PolicyCreatedEvents
 * as they arrive through the ActiveMQ message queue. It processes these events by updating
 * the status of the corresponding insurance quote requests.
 * */
@Component
public class PolicyCreatedMessageConsumer {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private InsuranceQuoteRequestRepository insuranceQuoteRequestRepository;

	@JmsListener(destination = "${policyCreatedEvent.queueName}")
	public void receivePolicyCreatedEvent(final Message<PolicyCreatedEvent> message) {
		logger.info("A new PolicyCreatedEvent has been received.");
		final PolicyCreatedEvent policyCreatedEvent = message.getPayload();
		final Long id = policyCreatedEvent.getInsuranceQuoteRequestId();
		final Optional<InsuranceQuoteRequestAggregateRoot> insuranceQuoteRequestOpt = insuranceQuoteRequestRepository.findById(id);

		if(!insuranceQuoteRequestOpt.isPresent()) {
			logger.error("Unable to process a policy created event with an invalid insurance quote request id.");
			return;
		}

		final InsuranceQuoteRequestAggregateRoot insuranceQuoteRequest = insuranceQuoteRequestOpt.get();
		insuranceQuoteRequest.finalizeQuote(policyCreatedEvent.getPolicyId(), policyCreatedEvent.getDate());
		logger.info("The insurance quote for insurance quote request " + insuranceQuoteRequest.getId() + " has expired.");
		insuranceQuoteRequestRepository.save(insuranceQuoteRequest);
	}
}