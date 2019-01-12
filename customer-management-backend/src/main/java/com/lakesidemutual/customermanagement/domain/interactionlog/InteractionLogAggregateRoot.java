package com.lakesidemutual.customermanagement.domain.interactionlog;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.microserviceapipatterns.domaindrivendesign.RootEntity;

/**
 * InteractionLogAggregateRoot is the root entity of the InteractionLog aggregate. Note that there is
 * no class for the InteractionLog aggregate, so the package can be seen as aggregate.
 */
@Entity
@Table(name = "interactionlogs")
public class InteractionLogAggregateRoot implements RootEntity {
	@Id
	private String customerId;

	private String username;

	private String lastAcknowledgedInteractionId;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private final Collection<InteractionEntity> interactions;

	public InteractionLogAggregateRoot() {
		this.customerId = null;
		this.username = null;
		this.lastAcknowledgedInteractionId = null;
		this.interactions = null;
	}

	public InteractionLogAggregateRoot(String customerId, String username, String lastAcknowledgedInteractionId, Collection<InteractionEntity> interactions) {
		this.customerId = customerId;
		this.username = username;
		this.lastAcknowledgedInteractionId = lastAcknowledgedInteractionId;
		this.interactions = interactions;
	}

	public String getCustomerId() {
		return customerId;
	}

	public String getUsername() {
		return username;
	}

	public String getLastAcknowledgedInteractionId() {
		return lastAcknowledgedInteractionId;
	}

	public void setLastAcknowledgedInteractionId(String lastAcknowledgedInteractionId) {
		this.lastAcknowledgedInteractionId = lastAcknowledgedInteractionId;
	}

	public Collection<InteractionEntity> getInteractions() {
		return interactions;
	}

	public int getNumberOfUnacknowledgedInteractions() {
		final List<InteractionEntity> interactions = getInteractions().stream().filter(interaction -> !interaction.isSentByOperator()).collect(Collectors.toList());
		if(lastAcknowledgedInteractionId == null) {
			return interactions.size();
		} else {
			int count = 0;
			for(int i = interactions.size()-1; i >= 0; i--) {
				if(lastAcknowledgedInteractionId.equals(interactions.get(i).getId())) {
					break;
				} else {
					count += 1;
				}
			}
			return count;
		}
	}
}
