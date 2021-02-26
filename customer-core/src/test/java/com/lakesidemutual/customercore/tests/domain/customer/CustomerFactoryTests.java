package com.lakesidemutual.customercore.tests.domain.customer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.lakesidemutual.customercore.domain.customer.Address;
import com.lakesidemutual.customercore.domain.customer.CustomerAggregateRoot;
import com.lakesidemutual.customercore.domain.customer.CustomerFactory;
import com.lakesidemutual.customercore.domain.customer.CustomerId;
import com.lakesidemutual.customercore.domain.customer.CustomerProfileEntity;
import com.lakesidemutual.customercore.infrastructure.CustomerRepository;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class CustomerFactoryTests {
	private final static CustomerId customerId = new CustomerId("rgpp0wkpec");
	private final static String firstName = "Max";
	private final static String lastName = "Mustermann";
	private final static Date birthday = new GregorianCalendar(1990, Calendar.JANUARY, 1).getTime();
	private final static String city = "Rapperswil";
	private final static String streetAddress = "Oberseestrasse 10";
	private final static String postalCode = "8640";
	private final static String formattedPhoneNumber = "055 222 41 11";
	private final static String unformattedPhoneNumber = "+41552224111";
	private final static String invalidPhoneNumber = "abc";
	private final static String email = "admin@example.com";

	@TestConfiguration
	static class CustomerFactoryTestContextConfiguration {
		@Bean
		public CustomerFactory customerFactory() {
			return new CustomerFactory();
		}
	}

	@Autowired
	private CustomerFactory customerFactory;

	@MockBean
	private CustomerRepository customerRepository;

	@Before
	public void setUp() {
		Mockito.when(customerRepository.nextId()).thenReturn(customerId);
	}

	@Test
	public void whenValidPhoneNumber_thenCustomerRootEntityShouldBeCreated() {
		Address address = new Address(streetAddress, postalCode, city);
		CustomerProfileEntity profile1 = new CustomerProfileEntity(firstName, lastName, birthday, address, email, formattedPhoneNumber);
		CustomerAggregateRoot customer = customerFactory.create(profile1);

		assertEquals(customerId, customer.getId());

		CustomerProfileEntity profile2 = customer.getCustomerProfile();
		assertEquals(firstName, profile2.getFirstname());
		assertEquals(lastName, profile2.getLastname());
		assertEquals(birthday, profile2.getBirthday());
		assertEquals(city, profile2.getCurrentAddress().getCity());
		assertEquals(streetAddress, profile2.getCurrentAddress().getStreetAddress());
		assertEquals(postalCode, profile2.getCurrentAddress().getPostalCode());
		assertEquals(formattedPhoneNumber, profile2.getPhoneNumber());
		assertEquals(email, profile2.getEmail());
		assertTrue(profile2.getMoveHistory().isEmpty());
	}

	@Test
	public void whenValidPhoneNumber_thenPhoneNumberIsFormatted() {
		Address address = new Address(streetAddress, postalCode, city);
		CustomerProfileEntity profile1 = new CustomerProfileEntity(firstName, lastName, birthday, address, email, unformattedPhoneNumber);
		CustomerAggregateRoot customer = customerFactory.create(profile1);

		assertEquals(customerId, customer.getId());

		CustomerProfileEntity profile2 = customer.getCustomerProfile();
		assertEquals(firstName, profile2.getFirstname());
		assertEquals(lastName, profile2.getLastname());
		assertEquals(birthday, profile2.getBirthday());
		assertEquals(city, profile2.getCurrentAddress().getCity());
		assertEquals(streetAddress, profile2.getCurrentAddress().getStreetAddress());
		assertEquals(postalCode, profile2.getCurrentAddress().getPostalCode());
		assertEquals(formattedPhoneNumber, profile2.getPhoneNumber());
		assertEquals(email, profile2.getEmail());
		assertTrue(profile2.getMoveHistory().isEmpty());
	}

	@Test
	public void whenInvalidPhoneNumber_thenAssertionErrorIsThrown() {
		boolean didThrowError = false;
		try {
			Address address = new Address(streetAddress, postalCode, city);
			CustomerProfileEntity profile = new CustomerProfileEntity(firstName, lastName, birthday, address, email, invalidPhoneNumber);
			customerFactory.create(profile);
		} catch(AssertionError error) {
			didThrowError = true;
		}

		if(!didThrowError) {
			fail();
		}
	}
}