package com.lakesidemutual.customermanagement.domain.interactionlog;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * InteractionEntity is an entity that contains the content and metadata of a chat message.
 */
@Entity
@Table(name = "interactions")
public class InteractionEntity implements org.microserviceapipatterns.domaindrivendesign.Entity {
	@Id
	private String id;
	private Date date;
	private String content;
	private boolean sentByOperator;

	public InteractionEntity() {}

	public InteractionEntity(String id, Date date, String content, boolean sentByOperator) {
		this.id = id;
		this.date = date;
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
