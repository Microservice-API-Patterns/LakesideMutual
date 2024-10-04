package com.lakesidemutual.policymanagement.interfaces.dtos.policy;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import com.lakesidemutual.policymanagement.domain.policy.InsuringAgreementItem;

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

	private InsuringAgreementItemDto(String title, String description) {
		this.title = title;
		this.description = description;
	}

	public static InsuringAgreementItemDto fromDomainObject(InsuringAgreementItem item) {
		return new InsuringAgreementItemDto(item.getTitle(), item.getDescription());
	}

	public InsuringAgreementItem toDomainObject() {
		return new InsuringAgreementItem(title, description);
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}
}
