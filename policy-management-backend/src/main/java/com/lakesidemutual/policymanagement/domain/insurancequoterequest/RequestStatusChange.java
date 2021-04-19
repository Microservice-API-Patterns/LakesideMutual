package com.lakesidemutual.policymanagement.domain.insurancequoterequest;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.microserviceapipatterns.domaindrivendesign.ValueObject;

/**
 * An instance of RequestStatusChange is a value object that represents a status change
 * of an insurance quote request. It contains the date of the status change as well as the new status.
 */
@Entity
@Table(name = "requeststatuschanges")
@Embeddable
public class RequestStatusChange implements ValueObject {
	@GeneratedValue
	@Id
	private Long id;

	private Date date;

	@Enumerated(EnumType.STRING)
	private RequestStatus status;

	public RequestStatusChange() {}

	public RequestStatusChange(Date date, RequestStatus status) {
		Objects.requireNonNull(date);
		Objects.requireNonNull(status);
		this.date = date;
		this.status = status;
	}

	public Date getDate() {
		return date;
	}

	public RequestStatus getStatus() {
		return status;
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

		RequestStatusChange other = (RequestStatusChange) obj;
		return Objects.equals(date, other.date) && Objects.equals(status, other.status);
	}

	@Override
	public int hashCode() {
		return Objects.hash(date, status);
	}
}