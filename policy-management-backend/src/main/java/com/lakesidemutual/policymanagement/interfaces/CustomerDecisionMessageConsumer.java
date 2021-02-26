package com.lakesidemutual.policymanagement.interfaces;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.lakesidemutual.policymanagement.domain.customer.CustomerId;
import com.lakesidemutual.policymanagement.domain.insurancequoterequest.CustomerDecisionEvent;
import com.lakesidemutual.policymanagement.domain.insurancequoterequest.CustomerInfoEntity;
import com.lakesidemutual.policymanagement.domain.insurancequoterequest.InsuranceQuoteExpiredEvent;
import com.lakesidemutual.policymanagement.domain.insurancequoterequest.InsuranceQuoteRequestAggregateRoot;
import com.lakesidemutual.policymanagement.domain.insurancequoterequest.PolicyCreatedEvent;
import com.lakesidemutual.policymanagement.domain.insurancequoterequest.RequestStatus;
import com.lakesidemutual.policymanagement.domain.policy.InsuringAgreementEntity;
import com.lakesidemutual.policymanagement.domain.policy.MoneyAmount;
import com.lakesidemutual.policymanagement.domain.policy.PolicyAggregateRoot;
import com.lakesidemutual.policymanagement.domain.policy.PolicyId;
import com.lakesidemutual.policymanagement.domain.policy.PolicyPeriod;
import com.lakesidemutual.policymanagement.domain.policy.PolicyType;
import com.lakesidemutual.policymanagement.domain.policy.UpdatePolicyEvent;
import com.lakesidemutual.policymanagement.infrastructure.CustomerCoreRemoteProxy;
import com.lakesidemutual.policymanagement.infrastructure.CustomerSelfServiceMessageProducer;
import com.lakesidemutual.policymanagement.infrastructure.InsuranceQuoteRequestRepository;
import com.lakesidemutual.policymanagement.infrastructure.PolicyRepository;
import com.lakesidemutual.policymanagement.infrastructure.RiskManagementMessageProducer;
import com.lakesidemutual.policymanagement.interfaces.dtos.customer.CustomerDto;
import com.lakesidemutual.policymanagement.interfaces.dtos.policy.PolicyDto;

/**
 * CustomerDecisionMessageConsumer is a Spring component that consumes CustomerDecisionEvents
 * as they arrive through the ActiveMQ message queue. It processes these events by updating the
 * corresponding insurance quote requests.
 * */
@Component
public class CustomerDecisionMessageConsumer {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private InsuranceQuoteRequestRepository insuranceQuoteRequestRepository;

	@Autowired
	private PolicyRepository policyRepository;

	@Autowired
	private CustomerSelfServiceMessageProducer customerSelfServiceMessageProducer;

	@Autowired
	private RiskManagementMessageProducer riskManagementMessageProducer;

	@Autowired
	private CustomerCoreRemoteProxy customerCoreRemoteProxy;

	@JmsListener(destination = "${customerDecisionEvent.queueName}")
	public void receiveCustomerDecision(final Message<CustomerDecisionEvent> message) {
		logger.debug("A new CustomerDecisionEvent has been received.");
		final CustomerDecisionEvent customerDecisionEvent = message.getPayload();
		final Long id = customerDecisionEvent.getInsuranceQuoteRequestId();
		final Optional<InsuranceQuoteRequestAggregateRoot> insuranceQuoteRequestOpt = insuranceQuoteRequestRepository.findById(id);

		if(!insuranceQuoteRequestOpt.isPresent()) {
			logger.error("Unable to process a customer decision event with an invalid insurance quote request id.");
			return;
		}

		final InsuranceQuoteRequestAggregateRoot insuranceQuoteRequest = insuranceQuoteRequestOpt.get();
		final Date decisionDate = customerDecisionEvent.getDate();

		if(customerDecisionEvent.isQuoteAccepted()) {
			if(insuranceQuoteRequest.getStatus().equals(RequestStatus.QUOTE_EXPIRED) || insuranceQuoteRequest.hasQuoteExpired(decisionDate)) {
				/*
				 * If the quote has been accepted after it has already expired, we mark the quote as accepted
				 * and expired and send a InsuranceQuoteExpiredEvent back to the Customer Self-Service backend.
				 * */
				Date expirationDate;
				if(insuranceQuoteRequest.getStatus().equals(RequestStatus.QUOTE_EXPIRED)) {
					expirationDate = insuranceQuoteRequest.popStatus().getDate();
				} else {
					expirationDate = decisionDate;
				}

				insuranceQuoteRequest.acceptQuote(decisionDate);
				insuranceQuoteRequest.markQuoteAsExpired(expirationDate);
				InsuranceQuoteExpiredEvent event = new InsuranceQuoteExpiredEvent(expirationDate, insuranceQuoteRequest.getId());
				customerSelfServiceMessageProducer.sendInsuranceQuoteExpiredEvent(event);
			} else {
				logger.info("The insurance quote for request {} has been accepted", insuranceQuoteRequest.getId());
				insuranceQuoteRequest.acceptQuote(decisionDate);
				PolicyAggregateRoot policy = createPolicyForInsuranceQuoteRequest(insuranceQuoteRequest);
				String policyId = policy.getId().getId();
				policyRepository.save(policy);
				Date policyCreationDate = new Date();
				insuranceQuoteRequest.finalizeQuote(policyId, policyCreationDate);

				PolicyCreatedEvent policyCreatedEvent = new PolicyCreatedEvent(policyCreationDate, insuranceQuoteRequest.getId(), policyId);
				customerSelfServiceMessageProducer.sendPolicyCreatedEvent(policyCreatedEvent);

				CustomerInfoEntity customerInfo = insuranceQuoteRequest.getCustomerInfo();
				List<CustomerDto> customers = customerCoreRemoteProxy.getCustomersById(customerInfo.getCustomerId());
				if(!customers.isEmpty()) {
					CustomerDto customer = customers.get(0);
					final PolicyDto policyDto = PolicyDto.fromDomainObject(policy);
					final UpdatePolicyEvent event = new UpdatePolicyEvent("<customer-self-service-backend>", decisionDate, customer, policyDto);
					riskManagementMessageProducer.emitEvent(event);
				}
			}
		} else {
			/*
			 * If a quote has been rejected by the customer after it has already expired,
			 * we discard the QUOTE_EXPIRED status in favor of the QUOTE_REJECTED status.
			 * */
			if(insuranceQuoteRequest.getStatus().equals(RequestStatus.QUOTE_EXPIRED)) {
				insuranceQuoteRequest.popStatus();
			}

			logger.info("The insurance quote for request {} has been rejected", insuranceQuoteRequest.getId());
			insuranceQuoteRequest.rejectQuote(decisionDate);
		}

		insuranceQuoteRequestRepository.save(insuranceQuoteRequest);
	}

	private PolicyAggregateRoot createPolicyForInsuranceQuoteRequest(InsuranceQuoteRequestAggregateRoot insuranceQuoteRequest) {
		PolicyId policyId = PolicyId.random();
		CustomerId customerId = insuranceQuoteRequest.getCustomerInfo().getCustomerId();

		Date startDate = insuranceQuoteRequest.getInsuranceOptions().getStartDate();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		calendar.add(Calendar.YEAR, 1);
		Date endDate = calendar.getTime();
		PolicyPeriod policyPeriod = new PolicyPeriod(startDate, endDate);

		PolicyType policyType = new PolicyType(insuranceQuoteRequest.getInsuranceOptions().getInsuranceType().getName());
		MoneyAmount deductible = insuranceQuoteRequest.getInsuranceOptions().getDeductible();
		MoneyAmount insurancePremium = insuranceQuoteRequest.getInsuranceQuote().getInsurancePremium();
		MoneyAmount policyLimit = insuranceQuoteRequest.getInsuranceQuote().getPolicyLimit();
		InsuringAgreementEntity insuringAgreement = new InsuringAgreementEntity(Collections.emptyList());
		return new PolicyAggregateRoot(policyId, customerId, new Date(), policyPeriod, policyType, deductible, policyLimit, insurancePremium, insuringAgreement);
	}
}
