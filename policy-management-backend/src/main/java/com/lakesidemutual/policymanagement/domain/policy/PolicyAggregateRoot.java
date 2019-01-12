package com.lakesidemutual.policymanagement.domain.policy;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.microserviceapipatterns.domaindrivendesign.RootEntity;
import com.lakesidemutual.policymanagement.domain.customer.CustomerId;

import io.github.adr.embedded.MADR;

/**
 * PolicyAggregateRoot is the root entity of the Policy aggregate. Note that there is
 * no class for the Policy aggregate, so the package can be seen as aggregate.
 */
@MADR(
		value = 2,
		title = "Separation between domain data model and infrastructure data model",
		contextAndProblem = "JPA / Spring Data annotations usually belong into a separate data model in the infrastructure layer",
		alternatives = {
				"Keep the JPA / Spring Data annotations in the domain data model",
				"Implement a separate data model with JPA / Spring Data annotations in the infrastructure layer",
		},
		chosenAlternative = "Keep the JPA / Spring Data annotations in the domain data model",
		justification = "The relatively small size of this application does not warrant the additional complexity (yet)."
		)
@Entity
@Table(name = "insurancepolicies")
public class PolicyAggregateRoot implements RootEntity {
	@EmbeddedId
	private PolicyId id;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name="id", column=@Column(name="customerId"))})
	private CustomerId customerId;

	/**
	 * When retrieving these entities, we want to be able to sort them by their creation date:
	 *
	 * policyRepository.findAll(Sort.by(Sort.Direction.DESC, PolicyAggregateRoot.FIELD_CREATION_DATE));
	 *
	 * Using a constant that contains the field name, we can don't have to use string literals to reference the field name. This also
	 * improves the maintainability of the code, if the name ever changes, we can just update the constant as well.
	 */
	public final static String FIELD_CREATION_DATE = "creationDate";

	private Date creationDate;

	@Embedded
	private PolicyPeriod policyPeriod;

	@Embedded
	private PolicyType policyType;

	/**
	 * These @AttributeOverrides attributes break the information hiding principle somewhat. However, we decided to keep them for now,
	 * because they are the easiest way to embed multiple MoneyAmount value objects into the PolicyAggregateRoot.
	 * */
	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name="amount", column=@Column(name="limitAmount")),
		@AttributeOverride(name="currency", column=@Column(name="limitCurrency"))})
	private MoneyAmount policyLimit;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name="amount", column=@Column(name="premiumAmount")),
		@AttributeOverride(name="currency", column=@Column(name="premiumCurrency"))})
	private MoneyAmount insurancePremium;

	@OneToOne(cascade = CascadeType.ALL)
	private InsuringAgreementEntity insuringAgreement;

	public PolicyAggregateRoot() {}

	public PolicyAggregateRoot(
			PolicyId id,
			CustomerId customerId,
			Date creationDate,
			PolicyPeriod policyPeriod,
			PolicyType policyType,
			MoneyAmount policyLimit,
			MoneyAmount insurancePremium,
			InsuringAgreementEntity insuringAgreement) {
		this.id = id;
		this.customerId = customerId;
		this.creationDate = creationDate;
		this.policyPeriod = policyPeriod;
		this.policyType = policyType;
		this.policyLimit = policyLimit;
		this.insurancePremium = insurancePremium;
		this.insuringAgreement = insuringAgreement;
	}

	public PolicyId getId() {
		return id;
	}

	public CustomerId getCustomerId() {
		return customerId;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public PolicyPeriod getPolicyPeriod() {
		return policyPeriod;
	}

	public PolicyType getPolicyType() {
		return policyType;
	}

	public MoneyAmount getPolicyLimit() {
		return policyLimit;
	}

	public MoneyAmount getInsurancePremium() {
		return insurancePremium;
	}

	public InsuringAgreementEntity getInsuringAgreement() {
		return insuringAgreement;
	}
}
