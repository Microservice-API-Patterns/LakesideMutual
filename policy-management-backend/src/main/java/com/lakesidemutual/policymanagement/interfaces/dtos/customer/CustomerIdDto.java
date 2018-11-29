package com.lakesidemutual.policymanagement.interfaces.dtos.customer;

/**
 * CustomerIdDto is a data transfer object (DTO) that represents the unique ID of a customer.
 * */
public class CustomerIdDto {
	private String id;

	public CustomerIdDto() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}
}