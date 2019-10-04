package com.lakesidemutual.customerselfservice.interfaces.dtos.identityaccess;

/**
 * AuthenticationRequestDto is a data transfer object (DTO) that represents the login credentials of a user.
 * It is sent to the AuthenticationController when a user tries to log into the Customer Self-Service frontend.
 */
public class AuthenticationRequestDto {
	private String email;
	private String password;

	public AuthenticationRequestDto() {
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
