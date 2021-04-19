package com.lakesidemutual.policymanagement.domain.insurancequoterequest;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.lakesidemutual.policymanagement.domain.policy.MoneyAmount;

/**
 * InsuranceOptionsEntity is an entity that contains the insurance options (e.g., start date, insurance type, etc.)
 * that a customer selected for an Insurance Quote Request.
 */
@Entity
@Table(name = "insuranceoptions")
public class InsuranceOptionsEntity implements org.microserviceapipatterns.domaindrivendesign.Entity {
	@GeneratedValue
	@Id
	private Long id;

	private Date startDate;

	@Embedded
	private InsuranceType insuranceType;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name="amount", column=@Column(name="deductibleAmount")),
		@AttributeOverride(name="currency", column=@Column(name="deductibleCurrency"))})
	private MoneyAmount deductible;

	public InsuranceOptionsEntity() {
	}

	public InsuranceOptionsEntity(Date startDate, InsuranceType insuranceType, MoneyAmount deductible) {
		this.startDate = startDate;
		this.insuranceType = insuranceType;
		this.deductible = deductible;
	}

	public Date getStartDate() {
		return startDate;
	}

	public InsuranceType getInsuranceType() {
		return insuranceType;
	}

	public MoneyAmount getDeductible() {
		return deductible;
	}
}
