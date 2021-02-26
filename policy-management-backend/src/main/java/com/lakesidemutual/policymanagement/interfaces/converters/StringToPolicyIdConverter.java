package com.lakesidemutual.policymanagement.interfaces.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.lakesidemutual.policymanagement.domain.policy.PolicyId;

/**
 * This converter class allows us to use PolicyId as the type of
 * a @PathVariable parameter in a Spring @RestController class.
 */
@Component
public class StringToPolicyIdConverter implements Converter<String, PolicyId> {
	@Override
	public PolicyId convert(String source) {
		return new PolicyId(source);
	}
}
