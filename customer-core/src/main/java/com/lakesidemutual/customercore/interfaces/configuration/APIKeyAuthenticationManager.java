package com.lakesidemutual.customercore.interfaces.configuration;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * The APIKeyAuthenticationManager ensures that only known clients can access the Customer Core API. It is an example of the <a href=
 * "http://www.microservice-api-patterns.org/patterns/quality/qualityManagementAndGovernance/WADE-APIKey.html">API Key</a> pattern
 * where each client identifies itself with an API Key. Example:
 * <br/>
 *
 * <br/>
 * <b>Missing API Key</b>
 * <pre>
 *   <code>
 * $ curl localhost:8110/customers
 * {
 *  "timestamp" : "2018-11-12T15:39:14.959+0000",
 *  "status" : 403,
 *  "error" : "Forbidden",
 *  "message" : "Access Denied",
 *  "path" : "/customers"
 * }
 *   </code>
 * </pre>
 *
 * <b>Valid API Key</b>
 * <pre>
 *   <code>
 * $ curl -H "Authorization: Bearer b318ad736c6c844b" localhost:8110/customers
 * {
 *  "filter" : "",
 *  "limit" : 10,
 *  "offset" : 0,
 *  "size" : 50,
 *  "customers" : [
 *  	...
 *  ]
 * }
 *   </code>
 * </pre>
 *
 * @see <a href=
 *      "http://www.microservice-api-patterns.org/patterns/quality/qualityManagementAndGovernance/WADE-APIKey.html">http://www.microservice-api-patterns.org/patterns/quality/qualityManagementAndGovernance/WADE-APIKey.html</a>
 */
public class APIKeyAuthenticationManager implements AuthenticationManager {
	private final List<String> validAPIKeys;

	public APIKeyAuthenticationManager(List<String> validAPIKeys) {
		this.validAPIKeys = validAPIKeys;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		final String bearerPrefix = "Bearer ";
		final String principal = (String)authentication.getPrincipal();
		if(!principal.startsWith(bearerPrefix)) {
			throw new BadCredentialsException("Invalid API Key");
		}

		final String apiKey = principal.substring(bearerPrefix.length());
		if(!validAPIKeys.contains(apiKey)) {
			throw new BadCredentialsException("Invalid API Key");
		}

		authentication.setAuthenticated(true);
		return authentication;
	}
}
