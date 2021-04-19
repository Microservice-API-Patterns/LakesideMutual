package com.lakesidemutual.customermanagement.interfaces.dtos;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * You might be wondering why this Exception class is in the dtos package. Exceptions of this type
 * are thrown whenever the Customer Management backend can't connect to the Customer Core. Spring will then
 * convert this exception into an HTTP 502 response.
 * */
@ResponseStatus(code = HttpStatus.BAD_GATEWAY)
public class CustomerCoreNotAvailableException extends RuntimeException {
	private static final long serialVersionUID = 2146599135907479601L;

	public CustomerCoreNotAvailableException(String errorMessage) {
		super(errorMessage);
	}
}