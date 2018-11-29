package com.lakesidemutual.customercore.interfaces.dtos.customer;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

import org.springframework.hateoas.ResourceSupport;

import com.lakesidemutual.customercore.domain.customer.Address;
import com.lakesidemutual.customercore.domain.customer.CustomerAggregateRoot;
import com.lakesidemutual.customercore.domain.customer.CustomerProfileEntity;

/**
 * The CustomerResponseDto represents a customer, including their complete move history. This is an example of the <a href=
 * "https://www.microservice-api-patterns.org/patterns/structure/basicRepresentationElements/WADE-ParameterTree.html">Parameter
 * Tree</a> pattern.
 */
public class CustomerResponseDto extends ResourceSupport {
	private final String customerId;

	private final String firstname;

	private final String lastname;

	private final Date birthday;

	private final String streetAddress;

	private final String postalCode;

	private final String city;

	private final String email;

	private final String phoneNumber;

	private final Collection<Address> moveHistory;

	public CustomerResponseDto(Set<String> includedFields, CustomerAggregateRoot customer) {
		this.customerId = select(includedFields, "customerId", customer.getId().getId());

		final CustomerProfileEntity profile = customer.getCustomerProfile();
		this.firstname = select(includedFields, "firstname", profile.getFirstname());
		this.lastname = select(includedFields, "lastname", profile.getLastname());
		this.birthday = select(includedFields, "birthday", profile.getBirthday());
		this.streetAddress = select(includedFields, "streetAddress", profile.getCurrentAddress().getStreetAddress());
		this.postalCode = select(includedFields, "postalCode", profile.getCurrentAddress().getPostalCode());
		this.city = select(includedFields, "city", profile.getCurrentAddress().getCity());
		this.email = select(includedFields, "email", profile.getEmail());
		this.phoneNumber = select(includedFields, "phoneNumber", profile.getPhoneNumber());
		this.moveHistory = select(includedFields, "moveHistory", profile.getMoveHistory());
	}

	private static <T> T select(Set<String> includedFields, String fieldName, T value) {
		if(includedFields.isEmpty() || includedFields.contains(fieldName)) {
			return value;
		} else {
			return null;
		}
	}

	public String getCustomerId() {
		return customerId;
	}

	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public Date getBirthday() {
		return birthday;
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

	public String getEmail() {
		return email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public Collection<Address> getMoveHistory() {
		return moveHistory;
	}
}