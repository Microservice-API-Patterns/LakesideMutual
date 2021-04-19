package com.lakesidemutual.customerselfservice.domain.insurancequoterequest;

import java.util.Date;

import com.lakesidemutual.customerselfservice.interfaces.dtos.insurancequoterequest.InsuranceQuoteRequestDto;
import org.microserviceapipatterns.domaindrivendesign.DomainEvent;

/**
 * InsuranceQuoteRequestEvent is a domain event class that is used to notify the Policy Management Backend
 * when a new Insurance Quote Request has been submitted by a customer.
 * */
public class InsuranceQuoteRequestEvent implements DomainEvent {
	private Date date;
	private InsuranceQuoteRequestDto insuranceQuoteRequestDto;

	public InsuranceQuoteRequestEvent() {
	}

	public InsuranceQuoteRequestEvent(Date date, InsuranceQuoteRequestDto insuranceQuoteRequestDto) {
		this.date = date;
		this.insuranceQuoteRequestDto = insuranceQuoteRequestDto;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public InsuranceQuoteRequestDto getInsuranceQuoteRequestDto() {
		return insuranceQuoteRequestDto;
	}

	public void setInsuranceQuoteRequestDto(InsuranceQuoteRequestDto insuranceQuoteRequestDto) {
		this.insuranceQuoteRequestDto = insuranceQuoteRequestDto;
	}
}
