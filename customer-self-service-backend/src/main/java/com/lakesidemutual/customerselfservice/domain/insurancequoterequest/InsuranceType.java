package com.lakesidemutual.customerselfservice.domain.insurancequoterequest;

import java.util.Objects;

import org.microserviceapipatterns.domaindrivendesign.ValueObject;

/**
 * An instance of InsuranceType is a value object that is used to represent the type of insurance (e.g., health insurance, life insurance, etc).
 */
public class InsuranceType implements ValueObject {
	private String name;

	public InsuranceType() {
		this.name = "";
	}

	public InsuranceType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
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

		InsuranceType other = (InsuranceType) obj;
		return Objects.equals(name, other.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
}