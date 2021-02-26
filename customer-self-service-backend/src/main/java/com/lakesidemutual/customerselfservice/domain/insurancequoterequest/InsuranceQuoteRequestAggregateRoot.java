package com.lakesidemutual.customerselfservice.domain.insurancequoterequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.microserviceapipatterns.domaindrivendesign.RootEntity;

/**
 * InsuranceQuoteRequestAggregateRoot is the root entity of the Insurance Quote Request aggregate. Note that there is
 * no class for the Insurance Quote Request aggregate, so the package can be seen as aggregate.
 */
@Entity
@Table(name = "insurancequoterequests")
public class InsuranceQuoteRequestAggregateRoot implements RootEntity {
	@Id
	@GeneratedValue
	private Long id;

	private Date date;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<RequestStatusChange> statusHistory;

	@OneToOne(cascade = CascadeType.ALL)
	private CustomerInfoEntity customerInfo;

	@OneToOne(cascade = CascadeType.ALL)
	private InsuranceOptionsEntity insuranceOptions;

	@OneToOne(cascade = CascadeType.ALL)
	private InsuranceQuoteEntity insuranceQuote;

	private String policyId;

	public InsuranceQuoteRequestAggregateRoot() {}

	public InsuranceQuoteRequestAggregateRoot(Date date, RequestStatus initialStatus, CustomerInfoEntity customerInfo, InsuranceOptionsEntity insuranceOptions, InsuranceQuoteEntity insuranceQuote, String policyId) {
		this.date = date;
		List<RequestStatusChange> statusHistory = new ArrayList<>();
		statusHistory.add(new RequestStatusChange(date, initialStatus));
		this.statusHistory = statusHistory;
		this.customerInfo = customerInfo;
		this.insuranceOptions = insuranceOptions;
		this.insuranceQuote = insuranceQuote;
		this.policyId = policyId;
	}

	public Long getId() {
		return id;
	}

	public Date getDate() {
		return date;
	}

	public RequestStatus getStatus() {
		return statusHistory.get(statusHistory.size()-1).getStatus();
	}

	public List<RequestStatusChange> getStatusHistory() {
		return statusHistory;
	}

	private void changeStatusTo(RequestStatus newStatus, Date date) {
		if (!getStatus().canTransitionTo(newStatus)) {
			throw new RuntimeException(String.format("Cannot change insurance quote request status from %s to %s", getStatus(), newStatus));
		}
		statusHistory.add(new RequestStatusChange(date, newStatus));
	}

	public void acceptRequest(InsuranceQuoteEntity insuranceQuote, Date date) {
		this.insuranceQuote = insuranceQuote;
		changeStatusTo(RequestStatus.QUOTE_RECEIVED, date);
	}

	public void rejectRequest(Date date) {
		changeStatusTo(RequestStatus.REQUEST_REJECTED, date);
	}

	public void acceptQuote(Date date) {
		changeStatusTo(RequestStatus.QUOTE_ACCEPTED, date);
	}

	public void rejectQuote(Date date) {
		changeStatusTo(RequestStatus.QUOTE_REJECTED, date);
	}

	public void markQuoteAsExpired(Date date) {
		changeStatusTo(RequestStatus.QUOTE_EXPIRED, date);
	}

	public void finalizeQuote(String policyId, Date date) {
		changeStatusTo(RequestStatus.POLICY_CREATED, date);
	}

	public CustomerInfoEntity getCustomerInfo() {
		return customerInfo;
	}

	public InsuranceOptionsEntity getInsuranceOptions() {
		return insuranceOptions;
	}

	public InsuranceQuoteEntity getInsuranceQuote() {
		return insuranceQuote;
	}

	public String getPolicyId() {
		return policyId;
	}
}
