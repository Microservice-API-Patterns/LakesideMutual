package com.lakesidemutual.policymanagement.domain.customer;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;

import org.microserviceapipatterns.domaindrivendesign.EntityIdentifier;
import org.microserviceapipatterns.domaindrivendesign.ValueObject;

import io.github.adr.embedded.MADR;

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
