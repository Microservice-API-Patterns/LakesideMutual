package com.lakesidemutual.policymanagement.interfaces.dtos;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * You might be wondering why this Exception class is in the dtos package. Exceptions of this type
 * are thrown whenever the client tries to create a policy for an unknown customer.
 * Spring will then convert this exception into an HTTP 424 response.
 * */
@ResponseStatus(code = HttpStatus.FAILED_DEPENDENCY)
public class UnknownCustomerException extends RuntimeException {
	private static final long serialVersionUID = -7187275179643436311L;

	public UnknownCustomerException(String errorMessage) {
		super(errorMessage);
	}
}