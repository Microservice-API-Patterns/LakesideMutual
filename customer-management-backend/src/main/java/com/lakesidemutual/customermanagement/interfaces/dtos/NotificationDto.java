package com.lakesidemutual.customermanagement.interfaces.dtos;

/**
 * NotificationDto is a data transfer object (DTO) that represents a chat notification in the Customer Management Frontend.
 * It describes how many as yet unacknowledged messages have been sent by a specific customer.
 */
public class NotificationDto {
	private String customerId;
	private String username;
	private int count;

	public NotificationDto() {}

	public NotificationDto(String customerId, String username, int count) {
		this.customerId = customerId;
		this.username = username;
		this.count = count;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}