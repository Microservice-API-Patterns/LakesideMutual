package com.lakesidemutual.policymanagement.domain.policy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * InsuringAgreementEntity is an entity which represents the insuring agreement between a
 * customer and Lakeside Mutual. Each InsuringAgreementEntity belongs to a PolicyAggregateRoot.
 */
@Entity
@Table(name = "insuringagreements")
public class InsuringAgreementEntity implements org.microserviceapipatterns.domaindrivendesign.Entity {
	@GeneratedValue
	@Id
	private Long id;

	@OneToMany(cascade = CascadeType.ALL)
	private final List<InsuringAgreementItem> agreementItems;

	public InsuringAgreementEntity() {
		this.agreementItems = null;
	}

	public InsuringAgreementEntity(List<InsuringAgreementItem> agreementItems) {
		this.agreementItems = agreementItems;
	}

	public Long getId() {
		return id;
	}

	public List<InsuringAgreementItem> getAgreementItems() {
		return agreementItems;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}

		InsuringAgreementEntity other = (InsuringAgreementEntity) obj;
		ArrayList<InsuringAgreementItem> lhs = new ArrayList<>(agreementItems);
		ArrayList<InsuringAgreementItem> rhs = new ArrayList<>(other.agreementItems);
		return lhs.equals(rhs);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(new ArrayList<>(agreementItems));
	}
}
