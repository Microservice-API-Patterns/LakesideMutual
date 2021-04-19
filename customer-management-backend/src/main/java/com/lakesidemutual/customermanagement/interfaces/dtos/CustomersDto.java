package com.lakesidemutual.customermanagement.interfaces.dtos;

import java.util.List;

import org.springframework.hateoas.ResourceSupport;

/**
 * The CustomersDto class is a data transfer object (DTO) that contains a list of customers.
 * It inherits from the ResourceSupport class which allows us to create a REST representation (e.g., JSON, XML)
 * that follows the HATEOAS principle. For example, links can be added to the representation (e.g., self, next, prev)
 * which means that future actions the client may take can be discovered from the resource representation.
 *
 * @see <a href="https://docs.spring.io/spring-hateoas/docs/current/reference/html/">Spring HATEOAS - Reference Documentation</a>
 */
public class CustomersDto extends ResourceSupport {
	private List<CustomerDto> customers;

	public CustomersDto() {}

	public CustomersDto(List<CustomerDto> customers) {
		this.customers = customers;
	}

	public List<CustomerDto> getCustomers() {
		return customers;
	}

	public void setCustomers(List<CustomerDto> customers) {
		this.customers = customers;
	}
}
