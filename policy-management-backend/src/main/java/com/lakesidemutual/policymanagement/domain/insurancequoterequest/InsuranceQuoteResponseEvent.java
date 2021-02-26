package com.lakesidemutual.policymanagement.domain.insurancequoterequest;

import java.util.Date;

import org.microserviceapipatterns.domaindrivendesign.DomainEvent;

import com.lakesidemutual.policymanagement.interfaces.dtos.policy.MoneyAmountDto;

/**
 * InsuranceQuoteResponseEvent is a domain event class that is used to notify the Customer Self-Service Backend
 * when Lakeside Mutual has submitted a response for a specific Insurance Quote Request.
 * */
public class InsuranceQuoteResponseEvent implements DomainEvent {
	private Date date;
	private Long insuranceQuoteRequestId;
	private boolean requestAccepted;
	private Date expirationDate;
	private MoneyAmountDto insurancePremium;
	private MoneyAmountDto policyLimit;

	public InsuranceQuoteResponseEvent() {
	}

	public InsuranceQuoteResponseEvent(Date date, Long insuranceQuoteRequestId, boolean requestAccepted, Date expirationDate, MoneyAmountDto insurancePremium, MoneyAmountDto policyLimit) {
		this.date = date;
		this.insuranceQuoteRequestId = insuranceQuoteRequestId;
		this.requestAccepted = requestAccepted;
		this.expirationDate = expirationDate;
		this.insurancePremium = insurancePremium;
		this.policyLimit = policyLimit;
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

	public boolean isRequestAccepted() {
		return requestAccepted;
	}

	public void setRequestAccepted(boolean requestAccepted) {
		this.requestAccepted = requestAccepted;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public MoneyAmountDto getInsurancePremium() {
		return insurancePremium;
	}

	public void setInsurancePremium(MoneyAmountDto insurancePremium) {
		this.insurancePremium = insurancePremium;
	}

	public MoneyAmountDto getPolicyLimit() {
		return policyLimit;
	}

	public void setPolicyLimit(MoneyAmountDto policyLimit) {
		this.policyLimit = policyLimit;
	}
}