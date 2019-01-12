package com.lakesidemutual.customercore.domain.customer;

import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.microserviceapipatterns.domaindrivendesign.RootEntity;

import io.github.adr.embedded.MADR;

/**
 * CustomerAggregateRoot is the root entity of the Customer aggregate. Note that there is
 * no class for the Customer aggregate, so the package can be seen as aggregate.
 */
@Entity
@Table(name = "customers")
public class CustomerAggregateRoot implements RootEntity {

	@EmbeddedId
	private CustomerId id;

	@Embedded
	private CustomerProfileEntity customerProfile;

	public CustomerAggregateRoot() {
	}

	public CustomerAggregateRoot(CustomerId id, CustomerProfileEntity customerProfile) {
		this.id = id;
		this.customerProfile = customerProfile;
	}

	public CustomerProfileEntity getCustomerProfile() {
		return customerProfile;
	}

	public CustomerId getId() {
		return id;
	}

	@MADR(
			value = 1,
			title = "Data transfer between interface layer and domain layer",
			contextAndProblem = "Need to pass information from the interfaces layer to the domain layer without introducing a layering violation",
			alternatives = {
					"Pass existing domain objects",
					"Pass the DTOs directly",
					"Pass the components of the DTO",
					"Add a new value type in the domain layer and use it as parameter object"
			},
			chosenAlternative = "Pass existing domain objects",
			justification = "This solution doesn't introduce a layering violation and it is simple because it doesn't require any additional classes."
			)
	public void moveToAddress(Address address) {
		customerProfile.moveToAddress(address);
	}

	public void updateCustomerProfile(CustomerProfileEntity updatedCustomerProfile) {
		customerProfile.setFirstname(updatedCustomerProfile.getFirstname());
		customerProfile.setLastname(updatedCustomerProfile.getLastname());
		customerProfile.setBirthday(updatedCustomerProfile.getBirthday());
		customerProfile.moveToAddress(updatedCustomerProfile.getCurrentAddress());
		customerProfile.setEmail(updatedCustomerProfile.getEmail());
		customerProfile.setPhoneNumber(CustomerFactory.formatPhoneNumber(updatedCustomerProfile.getPhoneNumber()));
	}
}
