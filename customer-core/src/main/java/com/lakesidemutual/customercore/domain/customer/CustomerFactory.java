package com.lakesidemutual.customercore.domain.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.lakesidemutual.customercore.infrastructure.CustomerRepository;
import org.microserviceapipatterns.domaindrivendesign.Factory;

/**
 * CustomerFactory is a factory that is responsible for the creation of CustomerAggregateRoot objects.
 * It makes sure that each new customer has a unique CustomerId and that phone numbers are formatted
 * correctly.
 * */
@Component
public class CustomerFactory implements Factory {
	@Autowired
	private CustomerRepository customerRepository;

	public CustomerAggregateRoot create(CustomerProfileEntity customerProfile) {
		CustomerId id = customerRepository.nextId();
		customerProfile.setPhoneNumber(formatPhoneNumber(customerProfile.getPhoneNumber()));
		return new CustomerAggregateRoot(id, customerProfile);
	}

	public static String formatPhoneNumber(String phoneNumberStr) {
		PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
		try {
			PhoneNumber phoneNumber = phoneUtil.parse(phoneNumberStr, "CH");
			return phoneUtil.format(phoneNumber, PhoneNumberFormat.NATIONAL);
		} catch (NumberParseException e) {
			throw new AssertionError();
		}
	}
}
