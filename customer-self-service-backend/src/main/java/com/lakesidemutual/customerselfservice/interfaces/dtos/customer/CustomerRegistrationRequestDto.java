package com.lakesidemutual.customerselfservice.interfaces.dtos.customer;

import java.util.Date;
import java.util.Objects;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import com.fasterxml.jackson.annotation.JsonFormat;
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

	@NotNull
	@Past
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
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		CustomerRegistrationRequestDto that = (CustomerRegistrationRequestDto) o;
		return Objects.equals(firstname, that.firstname) && Objects.equals(lastname, that.lastname) && Objects.equals(birthday, that.birthday) && Objects.equals(city, that.city) && Objects.equals(streetAddress, that.streetAddress) && Objects.equals(postalCode, that.postalCode) && Objects.equals(phoneNumber, that.phoneNumber);
	}

	@Override
	public int hashCode() {
		return Objects.hash(firstname, lastname, birthday, city, streetAddress, postalCode, phoneNumber);
	}
}
