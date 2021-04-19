package com.lakesidemutual.customerselfservice.interfaces.dtos.identityaccess;

/**
 * UserResponseDto is a data transfer object (DTO) that represents a user. The customerId property references the customer object in the
 * Customer Core that belongs to this user. A customerId that is set to null indicates that the user has not yet completed the registration.
 * */
public class UserResponseDto {
	private final String email;
	private final String customerId;

	public UserResponseDto(String email, String customerId) {
		this.email = email;
		this.customerId = customerId;
	}

	public String getEmail() {
		return email;
	}

	public String getCustomerId() {
		return customerId;
	}
}
