package com.lakesidemutual.customerselfservice.interfaces.dtos.insurancequoterequest;

import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.lakesidemutual.customerselfservice.domain.insurancequoterequest.InsuranceQuoteEntity;

/**
 * InsuranceQuoteDto is a data transfer object (DTO) that represents an Insurance Quote
 * which has been submitted as a response to a specific Insurance Quote Request.
 */
public class InsuranceQuoteDto {
	@Valid
	@NotNull
	private Date expirationDate;

	@Valid
	@NotNull
	private MoneyAmountDto insurancePremium;

	@Valid
	@NotNull
	private MoneyAmountDto policyLimit;

	public InsuranceQuoteDto() {
	}

	private InsuranceQuoteDto(Date expirationDate, MoneyAmountDto insurancePremium, MoneyAmountDto policyLimit) {
		this.expirationDate = expirationDate;
		this.insurancePremium = insurancePremium;
		this.policyLimit = policyLimit;
	}

	public static InsuranceQuoteDto fromDomainObject(InsuranceQuoteEntity insuranceQuote) {
		Date expirationDate = insuranceQuote.getExpirationDate();
		MoneyAmountDto insurancePremiumDto = MoneyAmountDto.fromDomainObject(insuranceQuote.getInsurancePremium());
		MoneyAmountDto policyLimitDto = MoneyAmountDto.fromDomainObject(insuranceQuote.getPolicyLimit());
		return new InsuranceQuoteDto(expirationDate, insurancePremiumDto, policyLimitDto);
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