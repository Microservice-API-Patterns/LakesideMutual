package com.lakesidemutual.customermanagement.domain.interactionlog;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lakesidemutual.customermanagement.infrastructure.InteractionLogRepository;
import org.microserviceapipatterns.domaindrivendesign.DomainService;

/**
 * InteractionLogService is a domain service which generates notification objects for any unacknowledged interactions.
 */
@Component
public class InteractionLogService implements DomainService {
	@Autowired
	private InteractionLogRepository interactionLogRepository;

	public List<Notification> getNotifications() {
		final List<Notification> notifications = new ArrayList<>();
		final List<InteractionLogAggregateRoot> interactionLogs = interactionLogRepository.findAll();
		for(InteractionLogAggregateRoot interactionLog : interactionLogs) {
			int count = interactionLog.getNumberOfUnacknowledgedInteractions();
			if(count > 0) {
				Notification notification = new Notification(interactionLog.getCustomerId(), interactionLog.getUsername(), count);
				notifications.add(notification);
			}
		}
		return notifications;
	}
}