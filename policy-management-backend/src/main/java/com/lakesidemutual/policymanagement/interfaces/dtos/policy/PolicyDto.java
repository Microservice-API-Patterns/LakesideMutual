package com.lakesidemutual.policymanagement.interfaces.dtos.policy;

import java.util.Date;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lakesidemutual.policymanagement.domain.policy.PolicyAggregateRoot;

/**
 * The PolicyDto class is a data transfer object (DTO) that represents a single insurance policy.
 * It inherits from the ResourceSupport class which allows us to create a REST representation (e.g., JSON, XML)
 * that follows the HATEOAS principle. For example, links can be added to the representation (e.g., self, address.change)
 * which means that future actions the client may take can be discovered from the resource representation.
 *
 * @see <a href="https://docs.spring.io/spring-hateoas/docs/current/reference/html/">Spring HATEOAS - Reference Documentation</a>
 */
public class PolicyDto extends RepresentationModel {
	private String policyId;
	private Object customer;
	private Date creationDate;
	private PolicyPeriodDto policyPeriod;
	private String policyType;
	private MoneyAmountDto deductible;
	private MoneyAmountDto policyLimit;
	private MoneyAmountDto insurancePremium;
	private InsuringAgreementDto insuringAgreement;
	@JsonProperty("_expandable")
	private String[] expandable;

	public PolicyDto() {}

	public PolicyDto(
			String policyId,
			Object customer,
			Date creationDate,
			PolicyPeriodDto policyPeriod,
			String policyType,
			MoneyAmountDto deductible,
			MoneyAmountDto policyLimit,
			MoneyAmountDto insurancePremium,
			InsuringAgreementDto insuringAgreement) {
		this.policyId = policyId;
		this.customer = customer;
		this.creationDate = creationDate;
		this.policyPeriod = policyPeriod;
		this.policyType = policyType;
		this.deductible = deductible;
		this.policyLimit = policyLimit;
		this.insurancePremium = insurancePremium;
		this.insuringAgreement = insuringAgreement;
		this.expandable = new String[]{"customer"};
	}

	public static PolicyDto fromDomainObject(PolicyAggregateRoot policy) {
		return new PolicyDto(
				policy.getId().getId(),
				policy.getCustomerId().getId(),
				policy.getCreationDate(),
				PolicyPeriodDto.fromDomainObject(policy.getPolicyPeriod()),
				policy.getPolicyType().getName(),
				MoneyAmountDto.fromDomainObject(policy.getDeductible()),
				MoneyAmountDto.fromDomainObject(policy.getPolicyLimit()),
				MoneyAmountDto.fromDomainObject(policy.getInsurancePremium()),
				InsuringAgreementDto.fromDomainObject(policy.getInsuringAgreement())
				);
	}

	public Object getCustomer() {
		return customer;
	}

	public void setCustomer(Object customer) {
		this.customer = customer;
	}

	public String getPolicyId() {
		return policyId;
	}

	public void setPolicyId(String policyId) {
		this.policyId = policyId;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public PolicyPeriodDto getPolicyPeriod() {
		return policyPeriod;
	}

	public void setPolicyPeriod(PolicyPeriodDto policyPeriod) {
		this.policyPeriod = policyPeriod;
	}

	public String getPolicyType() {
		return policyType;
	}

	public void setPolicyType(String policyType) {
		this.policyType = policyType;
	}

	public MoneyAmountDto getDeductible() {
		return deductible;
	}

	public void setDeductible(MoneyAmountDto deductible) {
		this.deductible = deductible;
	}

	public MoneyAmountDto getPolicyLimit() {
		return policyLimit;
	}

	public void setPolicyLimit(MoneyAmountDto policyLimit) {
		this.policyLimit = policyLimit;
	}

	public MoneyAmountDto getInsurancePremium() {
		return insurancePremium;
	}

	public void setInsurancePremium(MoneyAmountDto insurancePremium) {
		this.insurancePremium = insurancePremium;
	}

	public InsuringAgreementDto getInsuringAgreement() {
		return insuringAgreement;
	}

	public void setInsuringAgreement(InsuringAgreementDto insuringAgreement) {
		this.insuringAgreement = insuringAgreement;
	}

	public String[] getExpandable() {
		return expandable;
	}

	public void setExpandable(String[] expandable) {
		this.expandable = expandable;
	}
}
