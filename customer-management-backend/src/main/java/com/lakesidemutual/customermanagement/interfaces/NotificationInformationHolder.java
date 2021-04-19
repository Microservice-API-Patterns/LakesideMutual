package com.lakesidemutual.customermanagement.interfaces;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lakesidemutual.customermanagement.domain.interactionlog.InteractionLogService;
import com.lakesidemutual.customermanagement.interfaces.dtos.NotificationDto;

import io.swagger.annotations.ApiOperation;

/**
 * This REST controller gives clients access the current list of unacknowledged chat notifications. It is an example of the
 * <i>Information Holder Resource</i> pattern. This particular one is a special type of information holder called <i>Master Data Holder</i>.
 *
 * @see <a href="https://microservice-api-patterns.org/patterns/responsibility/endpointRoles/InformationHolderResource">Information Holder Resource</a>
 * @see <a href="https://microservice-api-patterns.org/patterns/responsibility/informationHolderEndpoints/MasterDataHolder">Master Data Holder</a>
 */
@RestController
@RequestMapping("/notifications")
public class NotificationInformationHolder {
	@Autowired
	private InteractionLogService interactionLogService;

	@ApiOperation(value = "Get a list of all unacknowledged notifications.")
	@GetMapping
	public ResponseEntity<List<NotificationDto>> getNotifications() {
		final List<NotificationDto> notifications = interactionLogService.getNotifications().stream()
				.map(notification -> new NotificationDto(notification.getCustomerId(), notification.getUsername(), notification.getCount()))
				.collect(Collectors.toList());
		return ResponseEntity.ok(notifications);
	}
}
