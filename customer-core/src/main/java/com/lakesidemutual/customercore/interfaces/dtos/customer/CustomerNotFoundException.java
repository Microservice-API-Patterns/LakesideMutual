package com.lakesidemutual.customercore.interfaces.dtos.customer;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * You might be wondering why this Exception class is in the dtos package. Exceptions of this type
 * are thrown whenever the client sends a request with an invalid customer id. Spring will then
 * convert this exception into an HTTP 404 response.
 * */
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class CustomerNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -402909143828880299L;

	public CustomerNotFoundException(String errorMessage) {
		super(errorMessage);
	}
}