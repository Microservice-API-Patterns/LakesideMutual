package com.lakesidemutual.policymanagement.interfaces.dtos.insurancequoterequest;

import com.lakesidemutual.policymanagement.domain.customer.CustomerId;
import com.lakesidemutual.policymanagement.domain.insurancequoterequest.CustomerInfoEntity;
import com.lakesidemutual.policymanagement.interfaces.dtos.customer.AddressDto;

/**
 * CustomerInfoDto is a data transfer object (DTO) that represents the
 * customer infos that are part of an Insurance Quote Request.
 * */
public class CustomerInfoDto {
	private String customerId;
	private String firstname;
	private String lastname;
	private AddressDto contactAddress;
	private AddressDto billingAddress;

	public CustomerInfoDto() {
	}

	private CustomerInfoDto(String customerId, String firstname, String lastname, AddressDto contactAddress, AddressDto billingAddress) {
		this.customerId = customerId;
		this.firstname = firstname;
		this.lastname = lastname;
		this.contactAddress = contactAddress;
		this.billingAddress = billingAddress;
	}

	public static CustomerInfoDto fromDomainObject(CustomerInfoEntity customerInfo) {
		String customerId = customerInfo.getCustomerId().getId();
		String firstname = customerInfo.getFirstname();
		String lastname = customerInfo.getLastname();
		AddressDto contactAddressDto = AddressDto.fromDomainObject(customerInfo.getContactAddress());
		AddressDto billingAddressDto = AddressDto.fromDomainObject(customerInfo.getBillingAddress());
		return new CustomerInfoDto(customerId, firstname, lastname, contactAddressDto, billingAddressDto);
	}

	public CustomerInfoEntity toDomainObject() {
		return new CustomerInfoEntity(new CustomerId(customerId), firstname, lastname, contactAddress.toDomainObject(), billingAddress.toDomainObject());
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public AddressDto getContactAddress() {
		return contactAddress;
	}

	public void setContactAddress(AddressDto contactAddress) {
		this.contactAddress = contactAddress;
	}

	public AddressDto getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(AddressDto billingAddress) {
		this.billingAddress = billingAddress;
	}
}