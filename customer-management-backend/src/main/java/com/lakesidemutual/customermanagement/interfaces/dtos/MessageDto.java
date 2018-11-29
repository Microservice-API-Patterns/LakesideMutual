package com.lakesidemutual.customermanagement.interfaces.dtos;

import java.util.Date;

import javax.validation.constraints.NotEmpty;

/**
 * MessageDto is a data transfer object (DTO) that contains the content and metadata of a chat message.
 */
public class MessageDto {
	private String id;

	private Date date;

	@NotEmpty
	private String customerId;

	@NotEmpty
	private String username;

	@NotEmpty
	private String content;

	private boolean sentByOperator;

	public MessageDto() {
	}

	public MessageDto(String id, Date date, String customerId, String username, String content, boolean sentByOperator) {
		this.id = id;
		this.date = date;
		this.customerId = customerId;
		this.username = username;
		this.content = content;
		this.sentByOperator = sentByOperator;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isSentByOperator() {
		return sentByOperator;
	}

	public void setSentByOperator(boolean sentByOperator) {
		this.sentByOperator = sentByOperator;
	}
}
