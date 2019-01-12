package com.lakesidemutual.customerselfservice.domain.identityaccess;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.lakesidemutual.customerselfservice.domain.customer.CustomerId;

/**
 * UserLogin is an entity that contains the login credentials of a specific user.
 */
@Entity
@Table(name = "user_logins")
public class UserLogin implements org.microserviceapipatterns.domaindrivendesign.Entity {

	@Id
	@GeneratedValue
	private Long id;
	private String authorities;
	private String email;
	private String password;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name="id", column=@Column(name="customerId"))})
	private CustomerId customerId;

	public UserLogin() {
	}

	public UserLogin(String email, String password, String authorities, CustomerId customerId) {
		this.email = email;
		this.password = password;
		this.authorities = authorities;
		this.customerId = customerId;
	}

	public String getAuthorities() {
		return authorities;
	}

	public CustomerId getCustomerId() {
		return customerId;
	}

	public String getEmail() {
		return email;
	}

	public Long getId() {
		return id;
	}

	public String getPassword() {
		return password;
	}

	public void setAuthorities(String authorities) {
		this.authorities = authorities;
	}

	public void setCustomerId(CustomerId customerId) {
		this.customerId = customerId;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
