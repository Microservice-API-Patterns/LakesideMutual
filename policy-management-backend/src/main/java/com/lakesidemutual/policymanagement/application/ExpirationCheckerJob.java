package com.lakesidemutual.policymanagement.application;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.lakesidemutual.policymanagement.domain.insurancequoterequest.InsuranceQuoteExpiredEvent;
import com.lakesidemutual.policymanagement.domain.insurancequoterequest.InsuranceQuoteRequestAggregateRoot;
import com.lakesidemutual.policymanagement.infrastructure.CustomerSelfServiceMessageProducer;
import com.lakesidemutual.policymanagement.infrastructure.InsuranceQuoteRequestRepository;

/**
 * ExpirationCheckerJob is a Quartz job that periodically checks for expired insurance quotes. For each
 * expired insurance quote, it also sends an InsuranceQuoteExpiredEvent to the Customer Self-Service backend.
 * */
public class ExpirationCheckerJob implements Job {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CustomerSelfServiceMessageProducer customerSelfServiceMessageProducer;

	@Autowired
	private InsuranceQuoteRequestRepository insuranceQuoteRequestRepository;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.debug("Checking for expired insurance quotes...");

		final Date date = new Date();
		List<InsuranceQuoteRequestAggregateRoot> quoteRequests = insuranceQuoteRequestRepository.findAll();
		List<InsuranceQuoteRequestAggregateRoot> expiredQuoteRequests = quoteRequests.stream()
				.filter(quoteRequest -> quoteRequest.checkQuoteExpirationDate(date))
				.collect(Collectors.toList());
		insuranceQuoteRequestRepository.saveAll(expiredQuoteRequests);
		expiredQuoteRequests.forEach(expiredQuoteRequest -> {
			InsuranceQuoteExpiredEvent event = new InsuranceQuoteExpiredEvent(date, expiredQuoteRequest.getId());
			customerSelfServiceMessageProducer.sendInsuranceQuoteExpiredEvent(event);
		});

		if(expiredQuoteRequests.size() > 0) {
			logger.info("Found {} expired insurance quotes", expiredQuoteRequests.size());
		} else {
			logger.debug("Found no expired insurance quotes");
		}
	}
}
