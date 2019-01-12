package com.lakesidemutual.policymanagement.domain.policy;

import java.util.Date;

import org.microserviceapipatterns.domaindrivendesign.DomainEvent;
import com.lakesidemutual.policymanagement.interfaces.dtos.customer.CustomerDto;
import com.lakesidemutual.policymanagement.interfaces.dtos.policy.PolicyDto;

/**
 * PolicyEvent is a domain event that is sent to the Risk Management Server
 * every time a new policy is created.
 * */
public class PolicyEvent implements DomainEvent {
	private String originator;
	private Date date;
	private CustomerDto customer;
	private PolicyDto policy;

	public PolicyEvent() {
	}

	public PolicyEvent(String originator, Date date, CustomerDto customer, PolicyDto policy) {
		this.originator = originator;
		this.date = date;
		this.customer = customer;
		this.policy = policy;
	}

	public String getOriginator() {
		return originator;
	}

	public void setOriginator(String originator) {
		this.originator = originator;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public CustomerDto getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerDto customer) {
		this.customer = customer;
	}

	public PolicyDto getPolicy() {
		return policy;
	}

	public void setPolicy(PolicyDto policy) {
		this.policy = policy;
	}
}
