package com.lakesidemutual.customermanagement.domain.interactionlog;

import org.microserviceapipatterns.domaindrivendesign.ValueObject;

/**
 * A Notification is a value object that is used to represent
 * the number of unread messages sent by a specific customer.
 * */
public class Notification implements ValueObject {
	private final String customerId;
	private final String username;
	private final int count;

	public Notification(String customerId, String username, int count) {
		this.customerId = customerId;
		this.username = username;
		this.count = count;
	}

	public String getCustomerId() {
		return customerId;
	}

	public String getUsername() {
		return username;
	}

	public int getCount() {
		return count;
	}
}