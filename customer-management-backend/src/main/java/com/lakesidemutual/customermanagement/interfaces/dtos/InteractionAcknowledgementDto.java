package com.lakesidemutual.customermanagement.interfaces.dtos;

import javax.validation.constraints.NotEmpty;

/**
 * InteractionAcknowledgementDto is a data transfer object (DTO) that is sent from the Customer Management Frontend
 * in order to acknowledge notifications up to a specific interaction. This means that the corresponding notification
 * disappears from the UI, because it was acknowledged/read.
 */
public class InteractionAcknowledgementDto {
	@NotEmpty
	private String lastAcknowledgedInteractionId;

	public InteractionAcknowledgementDto() {
	}

	public InteractionAcknowledgementDto(String lastAcknowledgedInteractionId) {
		this.lastAcknowledgedInteractionId = lastAcknowledgedInteractionId;
	}

	public String getLastAcknowledgedInteractionId() {
		return lastAcknowledgedInteractionId;
	}

	public void setLastAcknowledgedInteractionId(String lastAcknowledgedInteractionId) {
		this.lastAcknowledgedInteractionId = lastAcknowledgedInteractionId;
	}
}
