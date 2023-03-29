package com.lakesidemutual.customerselfservice.interfaces.dtos.swagger;

import java.util.Date;

/**
 * RiskFactorRequestDto is a data transfer object (DTO) that is sent to the Policy Management backend
 * when a client wants to compute the risk factor of a specific customer.
 * */
public class RiskFactorRequestDto {
	private Date birthday;
	private String postalCode;

	public RiskFactorRequestDto() {}

	public RiskFactorRequestDto(Date birthday, String postalCode) {
		this.setBirthday(birthday);
		this.setPostalCode(postalCode);
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
}
