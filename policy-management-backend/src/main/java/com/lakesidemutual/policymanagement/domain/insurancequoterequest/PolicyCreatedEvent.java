package com.lakesidemutual.policymanagement.domain.insurancequoterequest;

import java.util.Date;

import org.microserviceapipatterns.domaindrivendesign.DomainEvent;

/**
 * PolicyCreatedEvent is a domain event class that is used to notify the Customer Self-Service Backend
 * when a new Policy has been created after an Insurance Quote has been accepted.
 * */
public class PolicyCreatedEvent implements DomainEvent {
	private Date date;
	private Long insuranceQuoteRequestId;
	private String policyId;

	public PolicyCreatedEvent() {
	}

	public PolicyCreatedEvent(Date date, Long insuranceQuoteRequestId, String policyId) {
		this.date = date;
		this.insuranceQuoteRequestId = insuranceQuoteRequestId;
		this.policyId = policyId;
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

	public String getPolicyId() {
		return policyId;
	}

	public void setPolicyId(String policyId) {
		this.policyId = policyId;
	}
}