package com.lakesidemutual.customercore.interfaces.dtos.customer;

import java.util.List;

import org.springframework.hateoas.ResourceSupport;

/**
 * The CustomersResponseDto holds a collection of @CustomerResponseDto
 * Parameter Trees. This class is an example of the <a href=
 * "https://www.microservice-api-patterns.org/patterns/structure/basicRepresentationElements/WADE-ParameterForest.html">Parameter
 * Forest</a> pattern.
 */
public class CustomersResponseDto extends ResourceSupport {
	private final List<CustomerResponseDto> customers;

	public CustomersResponseDto(List<CustomerResponseDto> customers) {
		this.customers = customers;
	}

	public List<CustomerResponseDto> getCustomers() {
		return customers;
	}
}
