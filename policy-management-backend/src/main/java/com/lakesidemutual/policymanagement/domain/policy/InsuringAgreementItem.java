package com.lakesidemutual.policymanagement.domain.policy;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.microserviceapipatterns.domaindrivendesign.ValueObject;

/**
 * An InsuranceAgreementItem is a value object that is used to represent a single item in an insuring agreement.
 * */
@Entity
@Table(name = "insuranceagreementitems")
public class InsuringAgreementItem implements ValueObject {

	@GeneratedValue
	@Id
	private Long id;

	private final String title;

	private final String description;

	public InsuringAgreementItem() {
		this.title = null;
		this.description = null;
	}

	public InsuringAgreementItem(String title, String description) {
		this.title = title;
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
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

		InsuringAgreementItem other = (InsuringAgreementItem) obj;
		return Objects.equals(title, other.title) && Objects.equals(description, other.description);
	}

	@Override
	public int hashCode() {
		return Objects.hash(title, description);
	}
}
