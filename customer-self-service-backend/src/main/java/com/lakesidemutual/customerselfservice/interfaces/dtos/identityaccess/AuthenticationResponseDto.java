package com.lakesidemutual.customerselfservice.interfaces.dtos.identityaccess;

/**
 * AuthenticationResponseDto is a data transfer object (DTO) which contains the JWT token that is sent to a user
 * after he or she successfully logs into the Customer Self-Service frontend.
 */
public class AuthenticationResponseDto {
	private String email;
	private String token;

	public AuthenticationResponseDto(String email, String token) {
		this.email = email;
		this.token = token;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
