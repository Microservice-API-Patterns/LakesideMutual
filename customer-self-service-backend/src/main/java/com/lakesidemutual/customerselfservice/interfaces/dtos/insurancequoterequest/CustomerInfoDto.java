package com.lakesidemutual.customerselfservice.interfaces.dtos.insurancequoterequest;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.lakesidemutual.customerselfservice.domain.customer.CustomerId;
import com.lakesidemutual.customerselfservice.domain.insurancequoterequest.CustomerInfoEntity;
import com.lakesidemutual.customerselfservice.interfaces.dtos.customer.AddressDto;

/**
 * CustomerInfoDto is a data transfer object (DTO) that represents the
 * customer infos that are part of an Insurance Quote Request.
 * */
public class CustomerInfoDto {
	@NotEmpty
	private String customerId;

	@NotEmpty
	private String firstname;

	@NotEmpty
	private String lastname;

	@Valid
	@NotNull
	private AddressDto contactAddress;

	@Valid
	@NotNull
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