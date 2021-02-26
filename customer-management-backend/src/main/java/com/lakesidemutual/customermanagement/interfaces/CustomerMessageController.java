package com.lakesidemutual.customermanagement.interfaces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.lakesidemutual.customermanagement.domain.interactionlog.InteractionEntity;
import com.lakesidemutual.customermanagement.domain.interactionlog.InteractionLogAggregateRoot;
import com.lakesidemutual.customermanagement.domain.interactionlog.InteractionLogService;
import com.lakesidemutual.customermanagement.infrastructure.InteractionLogRepository;
import com.lakesidemutual.customermanagement.interfaces.dtos.MessageDto;
import com.lakesidemutual.customermanagement.interfaces.dtos.NotificationDto;

/**
 * This class is a Controller that processes WebSocket chat messages and broadcasts them to all subscribers.
 * */
@Controller
public class CustomerMessageController {
	private final static Logger logger = LoggerFactory.getLogger(CustomerMessageController.class);

	@Autowired
	private InteractionLogRepository interactionLogRepository;

	@Autowired
	private InteractionLogService interactionLogService;

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@MessageMapping("/chat/messages")
	@SendTo("/topic/messages")
	public MessageDto processMessage(MessageDto message) throws Exception {
		logger.info("Processing message from " + message.getUsername());

		final String customerId = message.getCustomerId();
		final String id = UUID.randomUUID().toString();
		final Date date = new Date();
		final InteractionEntity interaction = new InteractionEntity(id, date, message.getContent(), message.isSentByOperator());

		final Optional<InteractionLogAggregateRoot> optInteractionLog = interactionLogRepository.findById(customerId);
		InteractionLogAggregateRoot interactionLog;
		if (optInteractionLog.isPresent()) {
			interactionLog = optInteractionLog.get();
			interactionLog.getInteractions().add(interaction);
		} else {
			Collection<InteractionEntity> interactions = new ArrayList<>();
			interactions.add(interaction);
			interactionLog = new InteractionLogAggregateRoot(customerId, message.getUsername(), null, interactions);
		}

		interactionLogRepository.save(interactionLog);
		broadcastNotifications();
		return new MessageDto(id, date, message.getCustomerId(), message.getUsername(), message.getContent(), message.isSentByOperator());
	}

	private void broadcastNotifications() {
		logger.info("Broadcasting updated notifications");
		final List<NotificationDto> notifications = interactionLogService.getNotifications().stream()
				.map(notification -> new NotificationDto(notification.getCustomerId(), notification.getUsername(), notification.getCount()))
				.collect(Collectors.toList());
		simpMessagingTemplate.convertAndSend("/topic/notifications", notifications);
	}
}
