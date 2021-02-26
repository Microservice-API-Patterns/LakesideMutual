package com.lakesidemutual.customerselfservice.interfaces.dtos.customer;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * The CustomerDto class is a data transfer object (DTO) that represents a single customer.
 * It inherits from the ResourceSupport class which allows us to create a REST representation (e.g., JSON, XML)
 * that follows the HATEOAS principle. For example, links can be added to the representation (e.g., self, address.change)
 * which means that future actions the client may take can be discovered from the resource representation.
 *
 * @see <a href="https://docs.spring.io/spring-hateoas/docs/current/reference/html/">Spring HATEOAS - Reference Documentation</a>
 */
public class CustomerDto extends RepresentationModel {
	private String customerId;
	@JsonUnwrapped
	private CustomerProfileDto customerProfile;

	public CustomerDto() {
	}

	public String getCustomerId() {
		return customerId;
	}

	public CustomerProfileDto getCustomerProfile() {
		return this.customerProfile;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public void setCustomerProfile(CustomerProfileDto customerProfile) {
		this.customerProfile = customerProfile;
	}
}
