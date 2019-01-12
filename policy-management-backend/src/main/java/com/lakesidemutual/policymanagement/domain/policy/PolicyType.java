package com.lakesidemutual.policymanagement.domain.policy;

import java.util.Objects;

import org.microserviceapipatterns.domaindrivendesign.ValueObject;

/**
 * A PolicyType is a value object that is used to represent the type of a policy (e.g., health insurance, life insurance, etc).
 */
public class PolicyType implements ValueObject {
	private String name;

	public PolicyType() {
		this.name = "";
	}

	public PolicyType(String name) {
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

		PolicyType other = (PolicyType) obj;
		return Objects.equals(name, other.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
}
