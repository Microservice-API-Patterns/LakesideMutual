package com.lakesidemutual.policymanagement.interfaces.dtos.policy;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * You might be wondering why this Exception class is in the dtos package. Exceptions of this type
 * are thrown whenever the client sends a request with an invalid policy id. Spring will then
 * convert this exception into an HTTP 404 response.
 * */
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class PolicyNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -328902266721157272L;

	public PolicyNotFoundException(String errorMessage) {
		super(errorMessage);
	}
}
