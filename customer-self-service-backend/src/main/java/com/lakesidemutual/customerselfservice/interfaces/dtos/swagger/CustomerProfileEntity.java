package com.lakesidemutual.customerselfservice.interfaces.dtos.swagger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.google.common.base.Objects;
import org.microserviceapipatterns.domaindrivendesign.Entity;

/**
 * CustomerProfileEntity is an entity that contains the personal data (customer profile) of a CustomerAggregateRoot.
 */
@Embeddable
public class CustomerProfileEntity implements Serializable, Entity {

	private static final long serialVersionUID = 1L;

	private String firstname;

	private String lastname;

	private Date birthday;

	/**
	 * The usage of the javax.persistance annotations breaks the strict layering. We do this deliberately here, because the relatively small
	 * size of this application does not warrant the additional complexity of having a separate infrastructure data model (yet).
	 * */
	@OneToOne(cascade = CascadeType.ALL)
	private Address currentAddress;

	private String email;

	private String phoneNumber;

	@OneToMany(cascade = CascadeType.ALL)
	private Collection<Address> moveHistory;

	public CustomerProfileEntity() {
	}

	public CustomerProfileEntity(String firstname, String lastname, Date birthday, Address currentAddress, String email, String phoneNumber) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.birthday = birthday;
		this.currentAddress = currentAddress;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.moveHistory = new ArrayList<>();
	}

	public Date getBirthday() {
		return birthday;
	}

	public Address getCurrentAddress() {
		return currentAddress;
	}

	public String getEmail() {
		return email;
	}

	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public Collection<Address> getMoveHistory() {
		return moveHistory;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void moveToAddress(Address address) {
		if(!currentAddress.equals(address)) {
			moveHistory.add(currentAddress);
			setCurrentAddress(address);
		}
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public void setCurrentAddress(Address currentAddress) {
		this.currentAddress = currentAddress;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public void setMoveHistory(Collection<Address> moveHistory) {
		this.moveHistory = moveHistory;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		CustomerProfileEntity other = (CustomerProfileEntity)obj;
		return Objects.equal(firstname, other.firstname) &&
				Objects.equal(lastname, other.lastname) &&
				Objects.equal(birthday, other.birthday) &&
				Objects.equal(currentAddress, other.currentAddress) &&
				Objects.equal(moveHistory, other.moveHistory) &&
				Objects.equal(email, other.email) &&
				Objects.equal(phoneNumber, other.phoneNumber);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(firstname, lastname, birthday, currentAddress, email, phoneNumber);
	}
}