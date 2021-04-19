package com.lakesidemutual.customerselfservice.domain.insurancequoterequest;

import java.util.Date;

import org.microserviceapipatterns.domaindrivendesign.DomainEvent;

/**
 * InsuranceQuoteExpiredEvent is a domain event class that is used to notify the Customer Self-Service Backend
 * when the Insurance Quote for a specific Insurance Quote Request has expired.
 * */
public class InsuranceQuoteExpiredEvent implements DomainEvent {
	private Date date;
	private Long insuranceQuoteRequestId;

	public InsuranceQuoteExpiredEvent() {
	}

	public InsuranceQuoteExpiredEvent(Date date, Long insuranceQuoteRequestId) {
		this.date = date;
		this.insuranceQuoteRequestId = insuranceQuoteRequestId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Long getInsuranceQuoteRequestId() {
		return insuranceQuoteRequestId;
	}

	public void setInsuranceQuoteRequestId(Long insuranceQuoteRequestId) {
		this.insuranceQuoteRequestId = insuranceQuoteRequestId;
	}
}