package com.lakesidemutual.customercore.interfaces.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This @interface declaration defines a custom @PhoneNumber annotation which
 * can be used to validate phone numbers in a request DTO. Note that the
 * PhoneNumberValidator class performs the actual validation.
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneNumberValidator.class)
public @interface PhoneNumber {
	String message() default "must be a valid Swiss phone number (e.g, +4155 222 41 11)";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
