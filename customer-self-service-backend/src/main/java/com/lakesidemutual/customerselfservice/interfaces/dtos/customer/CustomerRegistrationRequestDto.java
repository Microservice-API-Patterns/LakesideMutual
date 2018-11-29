package com.lakesidemutual.customerselfservice.interfaces.dtos.customer;

import java.util.Date;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.base.Objects;
import com.lakesidemutual.customerselfservice.interfaces.validation.PhoneNumber;

/**
 * CustomerRegistrationRequestDto is a data transfer object (DTO) that represents the personal data (customer profile) of a customer.
 * It is sent to the CustomerInformationHolder when a user completes the registration process in the Customer Self-Service frontend.
 */
public class CustomerRegistrationRequestDto {

	@NotEmpty
	private String firstname;

	@NotEmpty
	private String lastname;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date birthday;

	@NotEmpty
	private String city;

	@NotEmpty
	private String streetAddress;

	@NotEmpty
	private String postalCode;

	@PhoneNumber
	private String phoneNumber;

	public CustomerRegistrationRequestDto() {
	}

	public CustomerRegistrationRequestDto(String firstname, String lastname, Date birthday, String city, String streetAddress, String postalCode, String phoneNumber) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.birthday = birthday;
		this.city = city;
		this.streetAddress = streetAddress;
		this.postalCode = postalCode;
		this.phoneNumber = phoneNumber;
	}

	public Date getBirthday() {
		return birthday;
	}

	public String getCity() {
		return city;
	}

	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
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

		CustomerRegistrationRequestDto other = (CustomerRegistrationRequestDto)obj;
		return Objects.equal(firstname, other.firstname) &&
				Objects.equal(lastname, other.lastname) &&
				Objects.equal(birthday, other.birthday) &&
				Objects.equal(city, other.city) &&
				Objects.equal(streetAddress, other.streetAddress) &&
				Objects.equal(postalCode, other.postalCode) &&
				Objects.equal(phoneNumber, other.phoneNumber);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(firstname, lastname, birthday, city, streetAddress, postalCode, phoneNumber);
	}
}
