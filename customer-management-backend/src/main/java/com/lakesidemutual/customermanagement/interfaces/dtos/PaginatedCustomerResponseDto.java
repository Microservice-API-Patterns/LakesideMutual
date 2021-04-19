package com.lakesidemutual.customermanagement.interfaces.dtos;

import java.util.List;

import org.springframework.hateoas.ResourceSupport;

/**
 * The PaginatedCustomerResponseDto holds a collection of CustomerDto
 * with additional metadata parameters such as the limit, offset and size that
 * are used in the <a href=
 * "https://microservice-api-patterns.org/patterns/structure/compositeRepresentations/Pagination">Pagination</a>
 * pattern, specifically the <em>Offset-Based</em> Pagination variant.
 */
public class PaginatedCustomerResponseDto extends ResourceSupport {
	private String filter;
	private int limit;
	private int offset;
	private int size;
	private List<CustomerDto> customers;

	public PaginatedCustomerResponseDto() {}

	public PaginatedCustomerResponseDto(String filter, int limit, int offset, int size, List<CustomerDto> customers) {
		this.filter = filter;
		this.limit = limit;
		this.offset = offset;
		this.size = size;
		this.customers = customers;
	}

	public List<CustomerDto> getCustomers() {
		return customers;
	}

	public void setCustomers(List<CustomerDto> customers) {
		this.customers = customers;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
}