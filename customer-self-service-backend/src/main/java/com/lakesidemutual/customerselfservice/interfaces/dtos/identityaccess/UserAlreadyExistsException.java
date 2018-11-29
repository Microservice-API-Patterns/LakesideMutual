package com.lakesidemutual.customerselfservice.interfaces.dtos.identityaccess;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * You might be wondering why this Exception class is in the dtos package. Exceptions of this type
 * are thrown whenever the client tries to create an account for a user that already exists.
 * Spring will then convert this exception into an HTTP 409 response.
 * */
@ResponseStatus(code = HttpStatus.CONFLICT)
public class UserAlreadyExistsException extends RuntimeException {
	private static final long serialVersionUID = 5852316265258762036L;

	public UserAlreadyExistsException(String errorMessage) {
		super(errorMessage);
	}
}