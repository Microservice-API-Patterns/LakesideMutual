package com.lakesidemutual.customerselfservice.interfaces.dtos.swagger;

import java.util.Date;

import javax.validation.constraints.NotNull;

/**
 * PolicyPeriodDto is a data transfer object (DTO) that represents the period during which a policy is valid.
 * */
public class PolicyPeriodDto {
	@NotNull
	private Date startDate;

	@NotNull
	private Date endDate;

	public PolicyPeriodDto() {
	}

	public PolicyPeriodDto(Date startDate, Date endDate) {
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public static PolicyPeriodDto fromDomainObject(PolicyPeriod policyPeriod) {
		return new PolicyPeriodDto(policyPeriod.getStartDate(), policyPeriod.getEndDate());
	}

	public PolicyPeriod toDomainObject() {
		return new PolicyPeriod(startDate, endDate);
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
