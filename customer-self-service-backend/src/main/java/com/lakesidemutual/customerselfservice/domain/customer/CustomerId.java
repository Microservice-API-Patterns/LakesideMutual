package com.lakesidemutual.customerselfservice.domain.customer;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;

import org.apache.commons.lang3.RandomStringUtils;

import org.microserviceapipatterns.domaindrivendesign.EntityIdentifier;
import org.microserviceapipatterns.domaindrivendesign.ValueObject;

/**
 * A CustomerId is a value object that is used to represent the unique id of a customer.
 */
@Embeddable
public class CustomerId implements Serializable, ValueObject, EntityIdentifier<String> {
	private static final long serialVersionUID = 1L;

	private String id;

	public CustomerId() {
		this.setId(null);
	}

	/**
	 * This constructor is needed by ControllerLinkBuilder, see the following
	 * spring-hateoas issue for details:
	 * https://github.com/spring-projects/spring-hateoas/issues/352
	 */
	public CustomerId(String id) {
		this.setId(id);
	}

	public static CustomerId random() {
		return new CustomerId(RandomStringUtils.randomAlphanumeric(10).toLowerCase());
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

		CustomerId other = (CustomerId) obj;
		return Objects.equals(getId(), other.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	@Override
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return getId();
	}
}
