package com.lakesidemutual.customerselfservice.interfaces.dtos.swagger;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Embeddable;

import org.microserviceapipatterns.domaindrivendesign.ValueObject;

/**
 * A PolicyPeriod is a value object that is used to represent the period during which a policy is valid.
 */
@Embeddable
public class PolicyPeriod implements ValueObject {
	private Date startDate;
	private Date endDate;

	public PolicyPeriod() {}

	public PolicyPeriod(Date startDate, Date endDate) {
		Objects.requireNonNull(startDate);
		Objects.requireNonNull(endDate);
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
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

		PolicyPeriod other = (PolicyPeriod) obj;
		if(startDate.getTime() != other.startDate.getTime()) {
			return false;
		}

		if(endDate.getTime() != other.endDate.getTime()) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		return Objects.hash(startDate, endDate);
	}
}
