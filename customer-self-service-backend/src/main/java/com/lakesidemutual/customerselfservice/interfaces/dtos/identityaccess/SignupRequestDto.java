package com.lakesidemutual.customerselfservice.interfaces.dtos.identityaccess;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 * SignupRequestDto is a data transfer object (DTO) that represents the login credentials of a new user. It is
 * sent to the AuthenticationController when a new user tries to sign up in the Customer Self-Service frontend.
 */
public class SignupRequestDto {
	@Email
	@NotEmpty
	private String email;

	@NotEmpty
	private String password;

	public SignupRequestDto() {
	}

	public SignupRequestDto(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return this.password;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
