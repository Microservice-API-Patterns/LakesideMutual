package com.lakesidemutual.customerselfservice.interfaces.dtos.swagger;

import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;

/**
 * InsuranceQuoteResponseDto is a data transfer object (DTO) that contains Lakeside Mutual's
 * response to a specific insurance quote request.
 * */
public class InsuranceQuoteResponseDto {
	@Valid
	@NotNull
	private String status;

	@Valid
	@Future
	private Date expirationDate;

	@Valid
	private MoneyAmountDto insurancePremium;

	@Valid
	private MoneyAmountDto policyLimit;

	public InsuranceQuoteResponseDto() {
	}

	public InsuranceQuoteResponseDto(String status, Date expirationDate, MoneyAmountDto insurancePremium, MoneyAmountDto policyLimit) {
		this.status = status;
		this.expirationDate = expirationDate;
		this.insurancePremium = insurancePremium;
		this.policyLimit = policyLimit;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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