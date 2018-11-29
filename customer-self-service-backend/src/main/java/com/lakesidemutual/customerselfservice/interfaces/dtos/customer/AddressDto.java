package com.lakesidemutual.customerselfservice.interfaces.dtos.customer;

import javax.validation.constraints.NotEmpty;

import com.lakesidemutual.customerselfservice.domain.customer.Address;

/**
 * AddressDto is a data transfer object (DTO) that represents the postal address of a customer.
 * */
public class AddressDto {
	@NotEmpty
	private String streetAddress;

	@NotEmpty
	private String postalCode;

	@NotEmpty
	private String city;

	public AddressDto() {
	}

	public AddressDto(String streetAddress, String postalCode, String city) {
		this.streetAddress = streetAddress;
		this.postalCode = postalCode;
		this.city = city;
	}

	public static AddressDto create(Address address) {
		return new AddressDto(address.getStreetAddress(), address.getPostalCode(), address.getCity());
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public String getCity() {
		return city;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public void setCity(String city) {
		this.city = city;
	}
}