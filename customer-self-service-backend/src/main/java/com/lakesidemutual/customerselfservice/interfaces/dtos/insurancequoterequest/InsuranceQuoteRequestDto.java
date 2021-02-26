package com.lakesidemutual.customerselfservice.interfaces.dtos.insurancequoterequest;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.lakesidemutual.customerselfservice.domain.insurancequoterequest.InsuranceQuoteRequestAggregateRoot;

/**
 * InsuranceQuoteRequestDto is a data transfer object (DTO) that represents a request
 * by a customer for a new insurance quote.
 */
public class InsuranceQuoteRequestDto {
	private Long id;

	@Valid
	private Date date;

	@Valid
	private List<RequestStatusChangeDto> statusHistory;

	@Valid
	@NotNull
	private CustomerInfoDto customerInfo;

	@Valid
	@NotNull
	private InsuranceOptionsDto insuranceOptions;

	@Valid
	private InsuranceQuoteDto insuranceQuote;

	private String policyId;

	public InsuranceQuoteRequestDto() {
	}

	public InsuranceQuoteRequestDto(Long id, Date date, List<RequestStatusChangeDto> statusHistory, CustomerInfoDto customerInfo, InsuranceOptionsDto insuranceOptions, InsuranceQuoteDto insuranceQuote, String policyId) {
		this.id = id;
		this.date = date;
		this.statusHistory = statusHistory;
		this.customerInfo = customerInfo;
		this.insuranceOptions = insuranceOptions;
		this.insuranceQuote = insuranceQuote;
		this.policyId = policyId;
	}

	public static InsuranceQuoteRequestDto fromDomainObject(InsuranceQuoteRequestAggregateRoot insuranceQuoteRequest) {
		Long id = insuranceQuoteRequest.getId();
		Date date = insuranceQuoteRequest.getDate();
		List<RequestStatusChangeDto> statusHistory = insuranceQuoteRequest.getStatusHistory().stream()
				.map(RequestStatusChangeDto::fromDomainObject)
				.collect(Collectors.toList());
		CustomerInfoDto customerInfoDto = CustomerInfoDto.fromDomainObject(insuranceQuoteRequest.getCustomerInfo());
		InsuranceOptionsDto insuranceOptionsDto = InsuranceOptionsDto.fromDomainObject(insuranceQuoteRequest.getInsuranceOptions());
		InsuranceQuoteDto insuranceQuoteDto = insuranceQuoteRequest.getInsuranceQuote() != null ? InsuranceQuoteDto.fromDomainObject(insuranceQuoteRequest.getInsuranceQuote()) : null;
		String policyId = insuranceQuoteRequest.getPolicyId();
		return new InsuranceQuoteRequestDto(id, date, statusHistory, customerInfoDto, insuranceOptionsDto, insuranceQuoteDto, policyId);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<RequestStatusChangeDto> getStatusHistory() {
		return statusHistory;
	}

	public void setStatusHistory(List<RequestStatusChangeDto> statusHistory) {
		this.statusHistory = statusHistory;
	}

	public CustomerInfoDto getCustomerInfo() {
		return customerInfo;
	}

	public void setCustomerInfo(CustomerInfoDto customerInfo) {
		this.customerInfo = customerInfo;
	}

	public InsuranceOptionsDto getInsuranceOptions() {
		return insuranceOptions;
	}

	public void setInsuranceOptions(InsuranceOptionsDto insuranceOptions) {
		this.insuranceOptions = insuranceOptions;
	}

	public InsuranceQuoteDto getInsuranceQuote() {
		return insuranceQuote;
	}

	public void setInsuranceQuote(InsuranceQuoteDto insuranceQuote) {
		this.insuranceQuote = insuranceQuote;
	}

	public String getPolicyId() {
		return policyId;
	}

	public void setPolicyId(String policyId) {
		this.policyId = policyId;
	}
}
