package com.lakesidemutual.customerselfservice.interfaces.dtos.swagger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
