package com.lakesidemutual.customercore.interfaces.dtos.customer;

import java.util.List;

import org.springframework.hateoas.RepresentationModel;

/**
 * The PaginatedCustomerResponseDto holds a collection of CustomerResponseDto
 * with additional control 
 * <a href="https://microservice-api-patterns.org/patterns/structure/elementStereotypes/MetadataElement">Metadata Elements</a> 
 * such as the limit, offset and size that are used in the 
 * <a href="https://www.microservice-api-patterns.org/patterns/structure/compositeRepresentations/Pagination">Pagination</a>
 * pattern, specifically the <em>Offset-Based</em> Pagination variant.
 */
public class PaginatedCustomerResponseDto extends RepresentationModel {
	private final String filter;
	private final int limit;
	private final int offset;
	private final int size;

	private final List<CustomerResponseDto> customers;

	public PaginatedCustomerResponseDto(String filter, int limit, int offset, int size,
			List<CustomerResponseDto> customers) {
		this.filter = filter;
		this.limit = limit;
		this.offset = offset;
		this.size = size;
		this.customers = customers;
	}

	public List<CustomerResponseDto> getCustomers() {
		return customers;
	}

	public String getFilter() {
		return filter;
	}

	public int getLimit() {
		return limit;
	}

	public int getOffset() {
		return offset;
	}

	public int getSize() {
		return size;
	}
}