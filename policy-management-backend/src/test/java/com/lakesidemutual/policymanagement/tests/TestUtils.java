package com.lakesidemutual.policymanagement.tests;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Currency;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.lakesidemutual.policymanagement.domain.customer.CustomerId;
import com.lakesidemutual.policymanagement.domain.policy.InsuringAgreementEntity;
import com.lakesidemutual.policymanagement.domain.policy.MoneyAmount;
import com.lakesidemutual.policymanagement.domain.policy.PolicyAggregateRoot;
import com.lakesidemutual.policymanagement.domain.policy.PolicyId;
import com.lakesidemutual.policymanagement.domain.policy.PolicyPeriod;
import com.lakesidemutual.policymanagement.domain.policy.PolicyType;
import com.lakesidemutual.policymanagement.interfaces.dtos.customer.AddressDto;
import com.lakesidemutual.policymanagement.interfaces.dtos.customer.CustomerDto;
import com.lakesidemutual.policymanagement.interfaces.dtos.customer.CustomerProfileDto;
import com.lakesidemutual.policymanagement.interfaces.dtos.policy.InsuringAgreementDto;
import com.lakesidemutual.policymanagement.interfaces.dtos.policy.MoneyAmountDto;
import com.lakesidemutual.policymanagement.interfaces.dtos.policy.PolicyDto;
import com.lakesidemutual.policymanagement.interfaces.dtos.policy.PolicyPeriodDto;

public final class TestUtils {
	private TestUtils() {}

	public static CustomerDto createTestCustomer(String customerIdStr, String firstname, String lastname, Date birthday, String streetAddress, String postalCode, String city, String email, String phoneNumber) {
		AddressDto address = new AddressDto();
		address.setStreetAddress(streetAddress);
		address.setPostalCode(postalCode);
		address.setCity(city);

		CustomerProfileDto customerProfile = new CustomerProfileDto();
		customerProfile.setFirstname(firstname);
		customerProfile.setLastname(lastname);
		customerProfile.setCurrentAddress(address);
		customerProfile.setEmail(email);
		customerProfile.setPhoneNumber(phoneNumber);
		customerProfile.setMoveHistory(new ArrayList<>());
		customerProfile.setBirthday(birthday);

		CustomerDto customer = new CustomerDto();
		customer.setCustomerId(customerIdStr);
		customer.setCustomerProfile(customerProfile);
		return customer;
	}

	public static PolicyAggregateRoot createTestPolicy(String policyIdStr, String customerIdStr, Date creationDate, Date startDate, Date endDate, BigDecimal policyLimitAmount, BigDecimal insurancePremiumAmount) {
		final PolicyId policyId = new PolicyId(policyIdStr);
		final CustomerId customerId = new CustomerId(customerIdStr);
		final PolicyPeriod policyPeriod = new PolicyPeriod(startDate, endDate);
		final PolicyType policyType = new PolicyType("Health Insurance");
		final Currency currency = Currency.getInstance("CHF");
		final MoneyAmount policyLimit = new MoneyAmount(policyLimitAmount, currency);
		final MoneyAmount insurancePremium = new MoneyAmount(insurancePremiumAmount, currency);
		final InsuringAgreementEntity insuringAgreement = new InsuringAgreementEntity(Collections.emptyList());
		return new PolicyAggregateRoot(policyId, customerId, creationDate, policyPeriod, policyType, policyLimit, insurancePremium, insuringAgreement);
	}

	public static PolicyDto createTestPolicyDto(String policyId, String customerId, Date creationDate, Date startDate, Date endDate, BigDecimal policyLimitAmount, BigDecimal insurancePremiumAmount) {
		final PolicyPeriodDto policyPeriod = new PolicyPeriodDto(startDate, endDate);
		final String policyType = "Health Insurance";
		final String currency = "CHF";
		final MoneyAmountDto policyLimit = new MoneyAmountDto(policyLimitAmount, currency);
		final MoneyAmountDto insurancePremium = new MoneyAmountDto(insurancePremiumAmount, currency);
		final InsuringAgreementDto insuringAgreement = new InsuringAgreementDto(Collections.emptyList());
		return new PolicyDto(policyId, customerId, creationDate, policyPeriod, policyType, policyLimit, insurancePremium, insuringAgreement);
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
}
