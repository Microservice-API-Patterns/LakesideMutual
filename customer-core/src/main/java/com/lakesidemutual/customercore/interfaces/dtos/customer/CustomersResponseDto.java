package com.lakesidemutual.customercore.interfaces.dtos.customer;

import java.util.List;

import org.springframework.hateoas.RepresentationModel;

/**
 * The CustomersResponseDto holds a collection of @CustomerResponseDto
 * Parameter Trees. This class is an example of the <a href=
 * "https://www.microservice-api-patterns.org/patterns/structure/representationElements/ParameterForest">Parameter
 * Forest</a> pattern.
 */
public class CustomersResponseDto extends RepresentationModel {
	private final List<CustomerResponseDto> customers;

	public CustomersResponseDto(List<CustomerResponseDto> customers) {
		this.customers = customers;
	}

	public List<CustomerResponseDto> getCustomers() {
		return customers;
	}
}
