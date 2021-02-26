package com.lakesidemutual.customercore.tests.domain.customer;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.lakesidemutual.customercore.domain.customer.Address;
import com.lakesidemutual.customercore.domain.customer.CustomerAggregateRoot;
import com.lakesidemutual.customercore.domain.customer.CustomerProfileEntity;
import com.lakesidemutual.customercore.tests.TestUtils;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class CustomerAggregateRootTests {
	private CustomerAggregateRoot customerA;

	@Before
	public void setUp() {
		customerA = TestUtils.createTestCustomer("rgpp0wkpec", "Max", "Mustermann",
				TestUtils.createDate(1, Calendar.JANUARY, 1990), "Oberseestrasse 10", "8640", "Rapperswil",
				"max@example.com", "055 222 41 11");
	}

	@Test
	public void whenAddressChanges_updateMoveHistory() {
		final String oldStreetAddress = "Oberseestrasse 10";
		final String oldPostalCode = "8640";
		final String oldCity = "Rapperswil";
		final String newStreetAddress = "Musterstrasse 1";
		final String newPostalCode = "1234";
		final String newCity = "Musterstadt";

		Address newAddress = new Address(newStreetAddress, newPostalCode, newCity);
		customerA.moveToAddress(newAddress);
		assertEquals(1, customerA.getCustomerProfile().getMoveHistory().size());

		Address oldAddress = customerA.getCustomerProfile().getMoveHistory().iterator().next();
		assertEquals(oldStreetAddress, oldAddress.getStreetAddress());
		assertEquals(oldPostalCode, oldAddress.getPostalCode());
		assertEquals(oldCity, oldAddress.getCity());

		assertEquals(newStreetAddress, customerA.getCustomerProfile().getCurrentAddress().getStreetAddress());
		assertEquals(newPostalCode, customerA.getCustomerProfile().getCurrentAddress().getPostalCode());
		assertEquals(newCity, customerA.getCustomerProfile().getCurrentAddress().getCity());
	}

	@Test
	public void whenAddressDoesntChange_dontUpdateMoveHistory() {
		final String oldStreetAddress = "Oberseestrasse 10";
		final String oldPostalCode = "8640";
		final String oldCity = "Rapperswil";

		Address oldAddress = new Address(oldStreetAddress, oldPostalCode, oldCity);
		customerA.moveToAddress(oldAddress);
		assertEquals(0, customerA.getCustomerProfile().getMoveHistory().size());

		assertEquals(oldStreetAddress, customerA.getCustomerProfile().getCurrentAddress().getStreetAddress());
		assertEquals(oldPostalCode, customerA.getCustomerProfile().getCurrentAddress().getPostalCode());
		assertEquals(oldCity, customerA.getCustomerProfile().getCurrentAddress().getCity());
	}

	@Test
	public void whenExistingCustomerId_thenUpdateCustomerProfile() {
		final String newFirstname = "Maxima";
		final String newLastname = "Musterfrau";
		final Date newBirthday = TestUtils.createDate(1, Calendar.JANUARY, 1990);
		final String oldStreetAddress = "Oberseestrasse 10";
		final String oldPostalCode = "8640";
		final String oldCity = "Rapperswil";
		final String newStreetAddress = "Musterstrasse 1";
		final String newPostalCode = "1234";
		final String newCity = "Musterstadt";
		final String newEmail = "maxima@example.com";
		final String newPhoneNumber = "055 222 41 11";

		Address newAddress = new Address(newStreetAddress, newPostalCode, newCity);
		CustomerProfileEntity updatedCustomerProfile = new CustomerProfileEntity(newFirstname, newLastname, newBirthday, newAddress, newEmail, newPhoneNumber);

		customerA.updateCustomerProfile(updatedCustomerProfile);

		CustomerProfileEntity customerProfile = customerA.getCustomerProfile();
		assertEquals(newFirstname, customerProfile.getFirstname());
		assertEquals(newLastname, customerProfile.getLastname());
		assertEquals(newBirthday, customerProfile.getBirthday());
		assertEquals(newStreetAddress, customerProfile.getCurrentAddress().getStreetAddress());
		assertEquals(newPostalCode, customerProfile.getCurrentAddress().getPostalCode());
		assertEquals(newCity, customerProfile.getCurrentAddress().getCity());
		assertEquals(newEmail, customerProfile.getEmail());
		assertEquals(newPhoneNumber, customerProfile.getPhoneNumber());

		assertEquals(1, customerA.getCustomerProfile().getMoveHistory().size());

		Address oldAddress = customerA.getCustomerProfile().getMoveHistory().iterator().next();
		assertEquals(oldStreetAddress, oldAddress.getStreetAddress());
		assertEquals(oldPostalCode, oldAddress.getPostalCode());
		assertEquals(oldCity, oldAddress.getCity());
	}
}
