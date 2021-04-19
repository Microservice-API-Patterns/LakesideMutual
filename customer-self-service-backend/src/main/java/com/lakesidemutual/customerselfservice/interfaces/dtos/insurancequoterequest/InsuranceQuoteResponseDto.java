package com.lakesidemutual.customerselfservice.interfaces.dtos.insurancequoterequest;

import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

/**
 * InsuranceQuoteResponseDto is a data transfer object (DTO) that contains Lakeside Mutual's
 * response to a specific insurance quote request.
 * */
public class InsuranceQuoteResponseDto {
	@NotEmpty
	private String status;

	@Valid
	private Date expirationDate;

	@Valid
	private MoneyAmountDto insurancePremium;

	@Valid
	private MoneyAmountDto policyLimit;

	public InsuranceQuoteResponseDto() {
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