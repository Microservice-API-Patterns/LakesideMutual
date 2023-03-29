package com.lakesidemutual.customerselfservice.interfaces.dtos.swagger;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * CreatePolicyRequestDto is a data transfer object (DTO) that is sent to the Policy Management backend when a
 * Lakeside Mutual employee creates a new policy for a customer.
 */
public class CreatePolicyRequestDto {
	@Valid
	private String customerId;

	@Valid
	private PolicyPeriodDto policyPeriod;

	@Valid
	@NotNull
	private String policyType;

	@Valid
	private MoneyAmountDto deductible;

	@Valid
	private MoneyAmountDto policyLimit;

	@Valid
	private MoneyAmountDto insurancePremium;

	@Valid
	private InsuringAgreementDto insuringAgreement;

	public CreatePolicyRequestDto() {}

	public CreatePolicyRequestDto(
			String customerId,
			PolicyPeriodDto policyPeriod,
			String policyType,
			MoneyAmountDto deductible,
			MoneyAmountDto policyLimit,
			MoneyAmountDto insurancePremium,
			InsuringAgreementDto insuringAgreement) {
		this.customerId = customerId;
		this.policyPeriod = policyPeriod;
		this.policyType = policyType;
		this.deductible = deductible;
		this.policyLimit = policyLimit;
		this.insurancePremium = insurancePremium;
		this.insuringAgreement = insuringAgreement;
	}

	public String getCustomerId() {
		return customerId;
	}

	public PolicyPeriodDto getPolicyPeriod() {
		return policyPeriod;
	}

	public String getPolicyType() {
		return policyType;
	}

	public MoneyAmountDto getDeductible() {
		return deductible;
	}

	public MoneyAmountDto getPolicyLimit() {
		return policyLimit;
	}

	public MoneyAmountDto getInsurancePremium() {
		return insurancePremium;
	}

	public InsuringAgreementDto getInsuringAgreement() {
		return insuringAgreement;
	}
}
