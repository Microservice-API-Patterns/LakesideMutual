package com.lakesidemutual.policymanagement.domain.policy;

import java.util.Date;

import org.microserviceapipatterns.domaindrivendesign.DomainEvent;

/**
 * DeletePolicyEvent is a domain event that is sent to the Risk Management Server
 * every time a policy is deleted.
 * */
public class DeletePolicyEvent implements DomainEvent {
	private String kind;
	private String originator;
	private Date date;
	private String policyId;

	public DeletePolicyEvent() {
	}

	public DeletePolicyEvent(String originator, Date date, String policyId) {
		this.kind = "DeletePolicyEvent";
		this.originator = originator;
		this.date = date;
		this.policyId = policyId;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
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

	public String getPolicyId() {
		return policyId;
	}

	public void setPolicyId(String policyId) {
		this.policyId = policyId;
	}
}
