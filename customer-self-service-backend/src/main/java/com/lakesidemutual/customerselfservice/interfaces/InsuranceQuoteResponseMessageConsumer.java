package com.lakesidemutual.customerselfservice.interfaces;

import java.util.Date;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.lakesidemutual.customerselfservice.domain.insurancequoterequest.InsuranceQuoteEntity;
import com.lakesidemutual.customerselfservice.domain.insurancequoterequest.InsuranceQuoteRequestAggregateRoot;
import com.lakesidemutual.customerselfservice.domain.insurancequoterequest.InsuranceQuoteResponseEvent;
import com.lakesidemutual.customerselfservice.domain.insurancequoterequest.MoneyAmount;
import com.lakesidemutual.customerselfservice.infrastructure.InsuranceQuoteRequestRepository;

/**
 * InsuranceQuoteResponseMessageConsumer is a Spring component that consumes InsuranceQuoteResponseEvents
 * as they arrive through the ActiveMQ message queue. It processes these events by updating the status
 * of the corresponding insurance quote requests.
 * */
@Component
public class InsuranceQuoteResponseMessageConsumer {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private InsuranceQuoteRequestRepository insuranceQuoteRequestRepository;

	@JmsListener(destination = "${insuranceQuoteResponseEvent.queueName}")
	public void receiveInsuranceQuoteResponse(final Message<InsuranceQuoteResponseEvent> message) {
		logger.info("A new InsuranceQuoteResponseEvent has been received.");
		final InsuranceQuoteResponseEvent insuranceQuoteResponseEvent = message.getPayload();
		final Long id = insuranceQuoteResponseEvent.getInsuranceQuoteRequestId();
		final Optional<InsuranceQuoteRequestAggregateRoot> insuranceQuoteRequestOpt = insuranceQuoteRequestRepository.findById(id);

		if(!insuranceQuoteRequestOpt.isPresent()) {
			logger.error("Unable to process an insurance quote response event with an invalid insurance quote request id.");
			return;
		}

		final Date date = insuranceQuoteResponseEvent.getDate();
		final InsuranceQuoteRequestAggregateRoot insuranceQuoteRequest = insuranceQuoteRequestOpt.get();
		if(insuranceQuoteResponseEvent.isRequestAccepted()) {
			logger.info("The insurance quote request " + insuranceQuoteRequest.getId() + " has been accepted.");
			Date expirationDate = insuranceQuoteResponseEvent.getExpirationDate();
			MoneyAmount insurancePremium = insuranceQuoteResponseEvent.getInsurancePremium().toDomainObject();
			MoneyAmount policyLimit = insuranceQuoteResponseEvent.getPolicyLimit().toDomainObject();
			InsuranceQuoteEntity insuranceQuote = new InsuranceQuoteEntity(expirationDate, insurancePremium, policyLimit);
			insuranceQuoteRequest.acceptRequest(insuranceQuote, date);
		} else {
			logger.info("The insurance quote request " + insuranceQuoteRequest.getId() + " has been rejected.");
			insuranceQuoteRequest.rejectRequest(date);
		}

		insuranceQuoteRequestRepository.save(insuranceQuoteRequest);
	}
}
