package com.lakesidemutual.policymanagement.interfaces.dtos.risk;

/**
 * RiskFactorResponseDto is a data transfer object (DTO) that contains the risk factor
 * that was computed for a specific customer.
 * */
public class RiskFactorResponseDto {
	private int riskFactor;

	public RiskFactorResponseDto(int riskFactor) {
		this.riskFactor = riskFactor;
	}

	public int getRiskFactor() {
		return riskFactor;
	}
}
