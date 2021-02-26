package com.lakesidemutual.customercore.interfaces.dtos.customer;

import javax.validation.constraints.NotEmpty;

import com.lakesidemutual.customercore.domain.customer.Address;

/**
 * The AddressDto represents the message payload to change a customer's address. This is an example of the <a href=
 * "https://www.microservice-api-patterns.org/patterns/structure/representationElements/AtomicParameterList">Atomic Parameter List</a> pattern.
 */
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
