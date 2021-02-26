package com.lakesidemutual.customerselfservice.domain.insurancequoterequest;

import java.util.Date;

import org.microserviceapipatterns.domaindrivendesign.DomainEvent;

/**
 * CustomerDecisionEvent is a domain event class that is used to notify the Policy Management Backend
 * about a decision by a customer to accept or reject a specific Insurance Quote.
 * */
public class CustomerDecisionEvent implements DomainEvent {
	private Date date;
	private Long insuranceQuoteRequestId;
	private boolean quoteAccepted;

	public CustomerDecisionEvent() {
	}

	public CustomerDecisionEvent(Date date, Long insuranceQuoteRequestId, boolean quoteAccepted) {
		this.date = date;
		this.insuranceQuoteRequestId = insuranceQuoteRequestId;
		this.quoteAccepted = quoteAccepted;
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

	public boolean isQuoteAccepted() {
		return quoteAccepted;
	}

	public void setQuoteAccepted(boolean quoteAccepted) {
		this.quoteAccepted = quoteAccepted;
	}
}
