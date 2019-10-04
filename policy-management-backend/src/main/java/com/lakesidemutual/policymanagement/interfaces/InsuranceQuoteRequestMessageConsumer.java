package com.lakesidemutual.policymanagement.interfaces;


import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.lakesidemutual.policymanagement.domain.insurancequoterequest.CustomerInfoEntity;
import com.lakesidemutual.policymanagement.domain.insurancequoterequest.InsuranceOptionsEntity;
import com.lakesidemutual.policymanagement.domain.insurancequoterequest.InsuranceQuoteRequestAggregateRoot;
import com.lakesidemutual.policymanagement.domain.insurancequoterequest.InsuranceQuoteRequestEvent;
import com.lakesidemutual.policymanagement.domain.insurancequoterequest.RequestStatus;
import com.lakesidemutual.policymanagement.infrastructure.InsuranceQuoteRequestRepository;
import com.lakesidemutual.policymanagement.interfaces.dtos.insurancequoterequest.InsuranceQuoteRequestDto;
import com.lakesidemutual.policymanagement.interfaces.dtos.insurancequoterequest.RequestStatusChangeDto;

/**
 * InsuranceQuoteRequestMessageConsumer is a Spring component that consumes InsuranceQuoteRequestEvents
 * as they arrive through the ActiveMQ message queue. It processes these events by creating corresponding
 * InsuranceQuoteRequestAggregateRoot instances.
 * */
@Component
public class InsuranceQuoteRequestMessageConsumer {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private InsuranceQuoteRequestRepository insuranceQuoteRequestRepository;

	@JmsListener(destination = "${insuranceQuoteRequestEvent.queueName}")
	public void receiveInsuranceQuoteRequest(final Message<InsuranceQuoteRequestEvent> message) {
		logger.info("A new InsuranceQuoteRequestEvent has been received.");

		InsuranceQuoteRequestEvent insuranceQuoteRequestEvent = message.getPayload();
		InsuranceQuoteRequestDto insuranceQuoteRequestDto = insuranceQuoteRequestEvent.getInsuranceQuoteRequestDto();
		Long id = insuranceQuoteRequestDto.getId();
		Date date = insuranceQuoteRequestDto.getDate();
		List<RequestStatusChangeDto> statusHistory = insuranceQuoteRequestDto.getStatusHistory();
		RequestStatus status = RequestStatus.valueOf(statusHistory.get(statusHistory.size()-1).getStatus());

		CustomerInfoEntity customerInfo = insuranceQuoteRequestDto.getCustomerInfo().toDomainObject();
		InsuranceOptionsEntity insuranceOptions = insuranceQuoteRequestDto.getInsuranceOptions().toDomainObject();

		InsuranceQuoteRequestAggregateRoot insuranceQuoteAggregateRoot = new InsuranceQuoteRequestAggregateRoot(id, date, status, customerInfo, insuranceOptions, null, null);
		insuranceQuoteRequestRepository.save(insuranceQuoteAggregateRoot);
	}
}
