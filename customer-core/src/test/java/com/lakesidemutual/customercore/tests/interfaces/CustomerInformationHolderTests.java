package com.lakesidemutual.customercore.tests.interfaces;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import com.lakesidemutual.customercore.application.CustomerService;
import com.lakesidemutual.customercore.domain.customer.Address;
import com.lakesidemutual.customercore.domain.customer.CustomerAggregateRoot;
import com.lakesidemutual.customercore.domain.customer.CustomerFactory;
import com.lakesidemutual.customercore.domain.customer.CustomerProfileEntity;
import com.lakesidemutual.customercore.infrastructure.CustomerRepository;
import com.lakesidemutual.customercore.interfaces.CustomerInformationHolder;
import com.lakesidemutual.customercore.interfaces.dtos.customer.CustomerProfileUpdateRequestDto;
import com.lakesidemutual.customercore.tests.TestUtils;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@WebMvcTest(value = CustomerInformationHolder.class, secure = false)
public class CustomerInformationHolderTests {
	private CustomerAggregateRoot customerA;
	private CustomerAggregateRoot customerB;
	private CustomerAggregateRoot customerC;

	@TestConfiguration
	static class AuthenticationControllerTestContextConfiguration {
		@Bean
		public HandlerInterceptor rateLimitInterceptor() {
			// This makes sure that the rate limiter is not active during unit-testing.
			return new HandlerInterceptor() {};
		}

		@Bean
		public CustomerService customerService() {
			return new CustomerService();
		}
	}

	@Autowired
	private MockMvc mvc;

	@MockBean
	private CustomerRepository customerRepository;

	@MockBean
	private CustomerFactory customerFactory;

	@Before
	public void setUp() {
		customerA = TestUtils.createTestCustomer("rgpp0wkpec", "Max", "Mustermann",
				TestUtils.createDate(1, Calendar.JANUARY, 1990), "Oberseestrasse 10", "8640", "Rapperswil",
				"max@example.com", "055 222 41 11");
		customerB = TestUtils.createTestCustomer("btpchn7eg8", "Hans", "Mustermann",
				TestUtils.createDate(1, Calendar.JANUARY, 1990), "Oberseestrasse 11", "8640", "Rapperswil",
				"hans@example.com", "055 222 41 12");
		customerC = TestUtils.createTestCustomer("5xvivyzxvc", "Anna", "Musterfrau",
				TestUtils.createDate(1, Calendar.JANUARY, 1990), "Oberseestrasse 12", "8640", "Rapperswil",
				"anna@example.com", "055 222 41 13");
	}

	@Test
	public void whenNewCustomerIsCreated_thenReturnNewCustomer() throws Exception {
		String firstname = "Max";
		String lastname = "Mustermann";
		Date birthday = TestUtils.createDate(1, Calendar.JANUARY, 1990);
		String streetAddress = "Oberseestrasse 10";
		String postalCode = "8640";
		String city = "Rapperswil";
		String email = "max@example.com";
		String phoneNumber = "055 222 41 11";

		CustomerProfileUpdateRequestDto registrationDto = new CustomerProfileUpdateRequestDto(
				firstname, lastname, birthday, streetAddress,
				postalCode, city, email, phoneNumber);
		Address currentAddress = new Address(streetAddress, postalCode, city);
		CustomerProfileEntity customerProfile = new CustomerProfileEntity(
				firstname, lastname, birthday, currentAddress,
				email, phoneNumber);

		Mockito.when(customerFactory.create(customerProfile)).thenReturn(customerA);

		mvc.perform(post("/customers").content(TestUtils.asJsonString(registrationDto))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(new CustomerResultMatcher("$", customerA));
	}

	@Test
	public void whenCustomersExist_thenGetAllCustomersShouldAllCustomers() throws Exception {
		Mockito.when(customerRepository.findAll(new Sort(Sort.Direction.ASC, "customerProfile.firstname", "customerProfile.lastname"))).thenReturn(Arrays.asList(customerA, customerB, customerC));

		mvc.perform(get("/customers"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.customers", hasSize(3)))
		.andExpect(new CustomerResultMatcher("$.customers[0]", customerA))
		.andExpect(new CustomerResultMatcher("$.customers[1]", customerB))
		.andExpect(new CustomerResultMatcher("$.customers[2]", customerC));
	}

	@Test
	public void whenExistingCustomerIdIsUsed_thenCustomerShouldBeReturned() throws Exception {
		Mockito.when(customerRepository.findById(customerA.getId())).thenReturn(Optional.of(customerA));
		Mockito.when(customerRepository.findById(customerB.getId())).thenReturn(Optional.of(customerB));
		Mockito.when(customerRepository.findById(customerC.getId())).thenReturn(Optional.of(customerC));

		mvc.perform(get("/customers/" + customerA.getId().toString()))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.customers", hasSize(1)))
		.andExpect(new CustomerResultMatcher("$.customers[0]", customerA));

		mvc.perform(get("/customers/" + customerB.getId().toString()))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.customers", hasSize(1)))
		.andExpect(new CustomerResultMatcher("$.customers[0]", customerB));

		mvc.perform(get("/customers/" + customerC.getId().toString()))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.customers", hasSize(1)))
		.andExpect(new CustomerResultMatcher("$.customers[0]", customerC));

		mvc.perform(get("/customers/" + customerA.getId().toString() + "," + customerB.getId().toString() + "," + customerC.getId().toString()))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.customers", hasSize(3)))
		.andExpect(new CustomerResultMatcher("$.customers[0]", customerA))
		.andExpect(new CustomerResultMatcher("$.customers[1]", customerB))
		.andExpect(new CustomerResultMatcher("$.customers[2]", customerC));
	}

	@Test
	public void whenExistingCustomerIdIsUsedWithFieldsParameter_thenCustomerFieldsShouldBeReturned() throws Exception {
		Mockito.when(customerRepository.findById(customerA.getId())).thenReturn(Optional.of(customerA));

		mvc.perform(get("/customers/" + customerA.getId().toString() + "/?fields=firstname"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.customers", hasSize(1)))
		.andExpect(jsonPath("$.customers[0].firstname", is(customerA.getCustomerProfile().getFirstname())))
		.andExpect(jsonPath("$.customers[0].lastname").doesNotExist())
		.andExpect(jsonPath("$.customers[0].streetAddress").doesNotExist())
		.andExpect(jsonPath("$.customers[0].email").doesNotExist());

		mvc.perform(get("/customers/" + customerA.getId().toString() + "/?fields=lastname,streetAddress,email"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.customers", hasSize(1)))
		.andExpect(jsonPath("$.customers[0].firstname").doesNotExist())
		.andExpect(jsonPath("$.customers[0].lastname", is(customerA.getCustomerProfile().getLastname())))
		.andExpect(jsonPath("$.customers[0].streetAddress", is(customerA.getCustomerProfile().getCurrentAddress().getStreetAddress())))
		.andExpect(jsonPath("$.customers[0].email", is(customerA.getCustomerProfile().getEmail())));
	}

	@Test
	public void whenNoCustomersExist_thenGetAllCustomersShouldReturnEmptyArray() throws Exception {
		mvc.perform(get("/customers")).andExpect(status().isOk())
		.andExpect(jsonPath("$.size", is(0)))
		.andExpect(jsonPath("$.customers", hasSize(0)));
	}

	@Test
	public void whenNonexistingCustomerIdIsUsed_thenEmptyArrayShouldBeReturned() throws Exception {
		mvc.perform(get("/customers/" + customerA.getId().toString()))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.customers", hasSize(0)));

		mvc.perform(get("/customers/" + customerB.getId().toString()))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.customers", hasSize(0)));

		mvc.perform(get("/customers/" + customerC.getId().toString()))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.customers", hasSize(0)));
	}

	@Test
	public void whenExistingCustomerIdIsUsed_thenCustomerProfileCanBeUpdated() throws Exception {
		CustomerAggregateRoot updatedCustomerA = TestUtils.createTestCustomer("rgpp0wkpec", "Maxima", "Musterfrau",
				TestUtils.createDate(1, Calendar.APRIL, 1990), "Musterstrasse 1", "1234", "Musterstadt",
				"maxima@example.com", "055 222 41 11");

		CustomerProfileEntity profile = updatedCustomerA.getCustomerProfile();
		Address address = profile.getCurrentAddress();
		CustomerProfileUpdateRequestDto profileUpdateRequestDto = new CustomerProfileUpdateRequestDto(
				profile.getFirstname(), profile.getLastname(), profile.getBirthday(), address.getStreetAddress(),
				address.getPostalCode(), address.getCity(), profile.getEmail(), profile.getPhoneNumber());

		Mockito.when(customerRepository.findById(customerA.getId())).thenReturn(Optional.of(customerA));

		mvc.perform(
				put("/customers/" + customerA.getId().getId())
				.content(TestUtils.asJsonString(profileUpdateRequestDto))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(new CustomerResultMatcher("$", updatedCustomerA));
	}

	@Test
	public void whenNonexistingCustomerIdIsUsed_thenCustomerProfileUpdateFails() throws Exception {
		CustomerAggregateRoot updatedCustomerA = TestUtils.createTestCustomer("rgpp0wkpec", "Maxima", "Musterfrau",
				TestUtils.createDate(1, Calendar.APRIL, 1990), "Musterstrasse 1", "1234", "Musterstadt",
				"maxima@example.com", "055 222 41 11");

		CustomerProfileEntity profile = updatedCustomerA.getCustomerProfile();
		CustomerProfileUpdateRequestDto profileUpdateRequestDto = new CustomerProfileUpdateRequestDto(
				profile.getFirstname(), profile.getLastname(), profile.getBirthday(), profile.getCurrentAddress().getStreetAddress(),
				profile.getCurrentAddress().getPostalCode(), profile.getCurrentAddress().getCity(), profile.getEmail(), profile.getPhoneNumber());

		mvc.perform(
				put("/customers/" + customerA.getId().getId())
				.content(TestUtils.asJsonString(profileUpdateRequestDto))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());
	}
}

final class CustomerResultMatcher implements ResultMatcher {
	private String jsonPathPrefix;
	private CustomerAggregateRoot customer;

	CustomerResultMatcher(String jsonPathPrefix, CustomerAggregateRoot customer) {
		this.jsonPathPrefix = jsonPathPrefix;
		this.customer = customer;
	}

	@Override
	public void match(MvcResult result) throws Exception {
		JsonMatcher jsonMatcher = new JsonMatcher(jsonPathPrefix);
		jsonMatcher.matchJson(result, ".customerId", customer.getId().toString());
		new CustomerProfileResultMatcher(jsonPathPrefix, customer.getCustomerProfile()).match(result);
	}
}

final class CustomerProfileResultMatcher implements ResultMatcher {
	private String jsonPathPrefix;
	private CustomerProfileEntity profile;

	CustomerProfileResultMatcher(String jsonPathPrefix, CustomerProfileEntity profile) {
		this.jsonPathPrefix = jsonPathPrefix;
		this.profile = profile;
	}

	@Override
	public void match(MvcResult result) throws Exception {
		JsonMatcher jsonMatcher = new JsonMatcher(jsonPathPrefix);
		jsonMatcher.matchJson(result, ".firstname", profile.getFirstname());
		jsonMatcher.matchJson(result, ".lastname", profile.getLastname());
		jsonMatcher.matchJson(result, ".birthday", TestUtils.createISO8601Timestamp(profile.getBirthday()));
		jsonMatcher.matchJson(result, ".streetAddress",
				profile.getCurrentAddress().getStreetAddress());
		jsonMatcher.matchJson(result, ".postalCode",
				profile.getCurrentAddress().getPostalCode());
		jsonMatcher.matchJson(result, ".city", profile.getCurrentAddress().getCity());
		jsonMatcher.matchJson(result, ".email", profile.getEmail());
		jsonMatcher.matchJson(result, ".phoneNumber", profile.getPhoneNumber());
	}
}

final class JsonMatcher {
	private String jsonPathPrefix;

	JsonMatcher(String jsonPathPrefix) {
		this.jsonPathPrefix = jsonPathPrefix;
	}

	<T> void matchJson(MvcResult result, String jsonPath, Matcher<T> matcher) throws Exception {
		jsonPath(jsonPathPrefix + jsonPath, matcher).match(result);
	}

	void matchJson(MvcResult result, String jsonPath, String expected) throws Exception {
		jsonPath(jsonPathPrefix + jsonPath, is(expected)).match(result);
	}
}
