package com.lakesidemutual.customerselfservice.interfaces.dtos.swagger;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

/**
 * This class validates phone numbers using Google's libphonenumber (see https://github.com/googlei18n/libphonenumber).
 * */
public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public boolean isValid(String phoneNumberStr, ConstraintValidatorContext context) {
		PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
		try {
			Phonenumber.PhoneNumber phoneNumber = phoneUtil.parse(phoneNumberStr, "CH");
			return phoneUtil.isValidNumber(phoneNumber);
		} catch (NumberParseException e) {
			logger.info("'" + phoneNumberStr + "' is not a valid phone number.", e);
			return false;
		}
	}
}
