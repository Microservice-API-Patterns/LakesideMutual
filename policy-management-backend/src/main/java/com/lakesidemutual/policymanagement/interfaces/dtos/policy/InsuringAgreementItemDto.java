package com.lakesidemutual.policymanagement.interfaces.dtos.policy;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

/**
 * InsuringAgreementItemDto is a data transfer object (DTO) that is used to represent a single item in an insuring agreement.
 */
public class InsuringAgreementItemDto {
	@Valid
	@NotEmpty
	private final String title;

	@Valid
	@NotEmpty
	private final String description;

	public InsuringAgreementItemDto() {
		this.title = null;
		this.description = null;
	}

	public InsuringAgreementItemDto(String title, String description) {
		this.title = title;
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}
}
