package com.lakesidemutual.customercore.tests;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lakesidemutual.customercore.domain.customer.Address;
import com.lakesidemutual.customercore.domain.customer.CustomerAggregateRoot;
import com.lakesidemutual.customercore.domain.customer.CustomerId;
import com.lakesidemutual.customercore.domain.customer.CustomerProfileEntity;

public final class TestUtils {
	private TestUtils() {}

	public static CustomerAggregateRoot createTestCustomer(String customerIdStr, String firstname, String lastname, Date birthday, String streetAddress, String postalCode, String city, String email, String phoneNumber) {
		CustomerId customerId = new CustomerId(customerIdStr);
		Address currentAddress = new Address(streetAddress, postalCode, city);
		CustomerProfileEntity customerProfile = new CustomerProfileEntity(firstname, lastname, birthday, currentAddress, email, phoneNumber);
		return new CustomerAggregateRoot(customerId, customerProfile);
	}

	public static String createISO8601Timestamp(Date date) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		return dateFormat.format(date);
	}

	public static Date createDate(int day, int month, int year) {
		GregorianCalendar calendar =  new GregorianCalendar(TimeZone.getTimeZone("UTC"));
		calendar.set(year, month, day, 0, 0, 0);
		calendar.clear(Calendar.MILLISECOND);
		return calendar.getTime();
	}

	public static String asJsonString(final Object obj) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			final String jsonContent = mapper.writeValueAsString(obj);
			return jsonContent;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
