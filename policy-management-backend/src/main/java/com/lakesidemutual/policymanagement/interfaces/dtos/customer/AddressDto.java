package com.lakesidemutual.policymanagement.interfaces.dtos.customer;

import com.lakesidemutual.policymanagement.domain.insurancequoterequest.Address;

/**
 * AddressDto is a data transfer object (DTO) that represents the postal address of a customer.
 * */
public class AddressDto {
	private String streetAddress;
	private String postalCode;
	private String city;

	public AddressDto() {
	}

	private AddressDto(String streetAddress, String postalCode, String city) {
		this.streetAddress = streetAddress;
		this.postalCode = postalCode;
		this.city = city;
	}

	public static AddressDto fromDomainObject(Address address) {
		return new AddressDto(address.getStreetAddress(), address.getPostalCode(), address.getCity());
	}

	public Address toDomainObject() {
		return new Address(streetAddress, postalCode, city);
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
