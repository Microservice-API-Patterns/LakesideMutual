package com.lakesidemutual.customerselfservice.interfaces.dtos.swagger;

import java.util.Objects;

import org.microserviceapipatterns.domaindrivendesign.ValueObject;

/**
 * An Address is a value object that is used to represent the postal address of
 * a customer.
 *
 * You might be wondering why the Address class implements the ValueObject
 * interface even though it has a JPA @Entity annotation.
 * This discrepancy exists for technical reasons. JPA requires Address to be
 * declared as an entity, because it is part of a one-to-many
 * relationship. However, in the DDD sense, Address behaves like a Value Object
 * (i.e., it has no id and is immutable).
 */
public class Address implements ValueObject {

	private Long id;

	private final String streetAddress;

	private final String postalCode;

	private final String city;

	public Address() {
		this.streetAddress = null;
		this.postalCode = null;
		this.city = null;
	}

	public Address(String streetAddress, String postalCode, String city) {
		this.streetAddress = streetAddress;
		this.postalCode = postalCode;
		this.city = city;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	@Override
	public String toString() {
		return String.format("%s, %s %ss", streetAddress, postalCode, city);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}

		Address other = (Address) obj;
		return Objects.equals(streetAddress, other.streetAddress) &&
				Objects.equals(postalCode, other.postalCode) &&
				Objects.equals(city, other.city);
	}

	@Override
	public int hashCode() {
		return Objects.hash(streetAddress, postalCode, city);
	}
}