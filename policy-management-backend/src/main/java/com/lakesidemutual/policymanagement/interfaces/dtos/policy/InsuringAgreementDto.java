package com.lakesidemutual.policymanagement.interfaces.dtos.policy;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * InsuringAgreementDto is a data transfer object (DTO) that represents the
 * insuring agreement between a customer and Lakeside Mutual.
 */
public class InsuringAgreementDto {
	@Valid
	@NotNull
	private final List<InsuringAgreementItemDto> agreementItems;

	public InsuringAgreementDto() {
		this.agreementItems = null;
	}

	public InsuringAgreementDto(List<InsuringAgreementItemDto> agreementItems) {
		this.agreementItems = agreementItems;
	}

	public List<InsuringAgreementItemDto> getAgreementItems() {
		return agreementItems;
	}
}
