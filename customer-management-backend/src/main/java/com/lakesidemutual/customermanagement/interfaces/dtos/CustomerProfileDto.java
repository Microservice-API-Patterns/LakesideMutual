package com.lakesidemutual.customermanagement.interfaces.dtos;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * CustomerProfileDto is a data transfer object (DTO) that represents the personal data (customer profile) of a customer.
 */
public class CustomerProfileDto {
	private String firstname;
	private String lastname;
	private Date birthday;
	@JsonUnwrapped
	private AddressDto currentAddress;
	private String email;
	private String phoneNumber;
	private List<AddressDto> moveHistory;

	public CustomerProfileDto() {
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

	public AddressDto getCurrentAddress() {
		return currentAddress;
	}

	public String getEmail() {
		return email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public List<AddressDto> getMoveHistory() {
		return moveHistory;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public void setCurrentAddress(AddressDto currentAddress) {
		this.currentAddress = currentAddress;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setMoveHistory(List<AddressDto> moveHistory) {
		this.moveHistory = moveHistory;
	}
}