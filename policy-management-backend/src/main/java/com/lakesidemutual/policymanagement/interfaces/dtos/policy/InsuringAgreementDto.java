package com.lakesidemutual.policymanagement.interfaces.dtos.policy;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.lakesidemutual.policymanagement.domain.policy.InsuringAgreementEntity;
import com.lakesidemutual.policymanagement.domain.policy.InsuringAgreementItem;

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

	public static InsuringAgreementDto fromDomainObject(InsuringAgreementEntity insuringAgreement) {
		List<InsuringAgreementItemDto> insuringAgreementItemDtos = insuringAgreement.getAgreementItems().stream()
				.map(InsuringAgreementItemDto::fromDomainObject)
				.collect(Collectors.toList());
		return new InsuringAgreementDto(insuringAgreementItemDtos);
	}

	public InsuringAgreementEntity toDomainObject() {
		List<InsuringAgreementItem> insuringAgreementItems = getAgreementItems().stream()
				.map(InsuringAgreementItemDto::toDomainObject)
				.collect(Collectors.toList());
		return new InsuringAgreementEntity(insuringAgreementItems);
	}

	public List<InsuringAgreementItemDto> getAgreementItems() {
		return agreementItems;
	}
}
