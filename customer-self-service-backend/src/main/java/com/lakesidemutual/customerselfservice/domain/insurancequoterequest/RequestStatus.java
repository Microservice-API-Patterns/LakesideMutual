package com.lakesidemutual.customerselfservice.domain.insurancequoterequest;

import org.microserviceapipatterns.domaindrivendesign.ValueObject;

/**
 * A RequestStatus is a value object that is used to represent
 * the current status of an insurance quote request.
 *
 * The following diagram shows the possible state transitions:
 *
 * <pre>
 *
 *                                               │
 *                                               ▼
 *                                        ┌────────────┐
 *                                        │  REQUEST_  │
 *                                        │ SUBMITTED  │
 *                                        └────────────┘
 *                                               │
 *                             ┌─────────────────┴────────────────┐
 *                             │                                  │
 *                             ▼                                  ▼
 *                      ┌────────────┐                     ╔════════════╗
 *                      │   QUOTE_   │                     ║  REQUEST_  ║
 *          ┌───────────│  RECEIVED  │─────────────┐       ║  REJECTED  ║
 *          │           └────────────┘             │       ╚════════════╝
 *          │                  │                   │
 *          │                  │                   │
 *          ▼                  ▼                   ▼
 *   ╔════════════╗     ┌────────────┐      ╔════════════╗
 *   ║   QUOTE_   ║     │   QUOTE_   │      ║   QUOTE_   ║
 *   ║  REJECTED  ║     │  ACCEPTED  │─────▶║  EXPIRED   ║
 *   ╚════════════╝     └────────────┘      ╚════════════╝
 *                             │
 *                             │
 *                             ▼
 *                      ╔════════════╗
 *                      ║  POLICY_   ║
 *                      ║  CREATED   ║
 *                      ╚════════════╝
 *
 * </pre>
 */
public enum RequestStatus implements ValueObject {
	/** The customer has submitted a request. No answer has been received yet. */
	REQUEST_SUBMITTED,

	/** Lakeside Mutual has rejected the request. No quote has been made. */
	REQUEST_REJECTED,

	/** Lakeside Mutual has accepted the request and made a corresponding quote. */
	QUOTE_RECEIVED,

	/** The customer has accepted Lakeside Mutual's quote. */
	QUOTE_ACCEPTED,

	/** The customer has rejected Lakeside Mutual's quote. */
	QUOTE_REJECTED,

	/** The quote has expired and is no longer valid. */
	QUOTE_EXPIRED,

	/** A new insurance policy has been created. */
	POLICY_CREATED;

	public boolean canTransitionTo(RequestStatus newStatus) {
		switch(this) {
		case REQUEST_SUBMITTED:
			return newStatus == REQUEST_REJECTED || newStatus == QUOTE_RECEIVED;
		case QUOTE_RECEIVED:
			return newStatus == QUOTE_ACCEPTED || newStatus == QUOTE_REJECTED || newStatus == QUOTE_EXPIRED;
		case QUOTE_ACCEPTED:
			return newStatus == POLICY_CREATED || newStatus == QUOTE_EXPIRED;
		case REQUEST_REJECTED:
		case POLICY_CREATED:
		case QUOTE_REJECTED:
		case QUOTE_EXPIRED:
			return false;
		default:
			return false;
		}
	}
}