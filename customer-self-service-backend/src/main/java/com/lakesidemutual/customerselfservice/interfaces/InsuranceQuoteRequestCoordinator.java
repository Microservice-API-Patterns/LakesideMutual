package com.lakesidemutual.customerselfservice.interfaces;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lakesidemutual.customerselfservice.domain.customer.CustomerId;
import com.lakesidemutual.customerselfservice.domain.identityaccess.UserLoginEntity;
import com.lakesidemutual.customerselfservice.domain.insurancequoterequest.CustomerInfoEntity;
import com.lakesidemutual.customerselfservice.domain.insurancequoterequest.InsuranceOptionsEntity;
import com.lakesidemutual.customerselfservice.domain.insurancequoterequest.InsuranceQuoteRequestAggregateRoot;
import com.lakesidemutual.customerselfservice.domain.insurancequoterequest.RequestStatus;
import com.lakesidemutual.customerselfservice.infrastructure.InsuranceQuoteRequestRepository;
import com.lakesidemutual.customerselfservice.infrastructure.PolicyManagementMessageProducer;
import com.lakesidemutual.customerselfservice.infrastructure.UserLoginRepository;
import com.lakesidemutual.customerselfservice.interfaces.dtos.insurancequoterequest.CustomerInfoDto;
import com.lakesidemutual.customerselfservice.interfaces.dtos.insurancequoterequest.InsuranceQuoteRequestDto;
import com.lakesidemutual.customerselfservice.interfaces.dtos.insurancequoterequest.InsuranceQuoteRequestNotFoundException;
import com.lakesidemutual.customerselfservice.interfaces.dtos.insurancequoterequest.InsuranceQuoteResponseDto;


/**
 * This REST controller gives clients access to the insurance quote requests. It is an example of the
 * <i>Information Holder Resource</i> pattern. This particular one is a special type of information holder called <i>Operational Data Holder</i>.
 *
 * @see <a href="https://www.microservice-api-patterns.org/patterns/responsibility/informationHolderEndpointTypes/OperationalDataHolder">Operational Data Holder</a>
 * 
 * Requests are created and updated here too, so it also is a Processing Resource:
 * 
 *  * @see <a href="https://www.microservice-api-patterns.org/patterns/responsibility/endpointRoles/ProcessingResource">Processing Resource</a>
 *  
 *Matching RDD role stereotypes are <i>Coordinator</i> and <i>Information Holder</i>:
 *
 *  * @see <a href="http://www.wirfs-brock.com/PDFs/A_Brief-Tour-of-RDD.pdf">A Brief Tour of RDD</a>
 */
@RestController
@RequestMapping("/insurance-quote-requests")
public class InsuranceQuoteRequestCoordinator {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private InsuranceQuoteRequestRepository insuranceQuoteRequestRepository;

	@Autowired
	private UserLoginRepository userLoginRepository;

	@Autowired
	private PolicyManagementMessageProducer policyManagementMessageProducer;

	/**
	 * This endpoint is only used for debugging purposes.
	 * */
	@Operation(summary = "Get all Insurance Quote Requests.")
	@GetMapping /* MAP: Retrieval Operation */ 
	public ResponseEntity<List<InsuranceQuoteRequestDto>> getInsuranceQuoteRequests() {
		List<InsuranceQuoteRequestAggregateRoot> quoteRequests = insuranceQuoteRequestRepository.findAllByOrderByDateDesc();
		List<InsuranceQuoteRequestDto> quoteRequestDtos = quoteRequests.stream().map(InsuranceQuoteRequestDto::fromDomainObject).collect(Collectors.toList());
		return ResponseEntity.ok(quoteRequestDtos);
	}

	@Operation(summary = "Get a specific Insurance Quote Request.")
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/{insuranceQuoteRequestId}") /* MAP: Retrieval Operation */ 
	public ResponseEntity<InsuranceQuoteRequestDto> getInsuranceQuoteRequest(
			Authentication authentication,
			@Parameter(description = "the insurance quote request's unique id", required = true) @PathVariable Long insuranceQuoteRequestId) {
		Optional<InsuranceQuoteRequestAggregateRoot> optInsuranceQuoteRequest = insuranceQuoteRequestRepository.findById(insuranceQuoteRequestId);
		if(!optInsuranceQuoteRequest.isPresent()) {
			final String errorMessage = "Failed to find an insurance quote request with id '" + insuranceQuoteRequestId + "'.";
			logger.info(errorMessage);
			throw new InsuranceQuoteRequestNotFoundException(errorMessage);
		}

		InsuranceQuoteRequestAggregateRoot insuranceQuoteRequest = optInsuranceQuoteRequest.get();
		CustomerId loggedInCustomerId = userLoginRepository.findByEmail(authentication.getName()).getCustomerId();
		if (!insuranceQuoteRequest.getCustomerInfo().getCustomerId().equals(loggedInCustomerId)) {
			logger.info("Can't access an Insurance Quote Request of a different customer.");
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		return ResponseEntity.ok(InsuranceQuoteRequestDto.fromDomainObject(insuranceQuoteRequest));
	}

	@Operation(summary = "Create a new Insurance Quote Request.")
	@PreAuthorize("isAuthenticated()")
	@PostMapping /* MAP: State Creation Operation */ 
	public ResponseEntity<InsuranceQuoteRequestDto> createInsuranceQuoteRequest(
			Authentication authentication,
			@Parameter(description = "the insurance quote request", required = true) @Valid @RequestBody InsuranceQuoteRequestDto requestDto) {
		String loggedInUserEmail = authentication.getName();
		UserLoginEntity loggedInUser = userLoginRepository.findByEmail(loggedInUserEmail);
		CustomerId loggedInCustomerId = loggedInUser.getCustomerId();
		if (loggedInCustomerId == null) {
			logger.info("Customer needs to complete registration first.");
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		CustomerInfoDto customerInfoDto = requestDto.getCustomerInfo();
		CustomerId customerId = new CustomerId(customerInfoDto.getCustomerId());

		if (!customerId.equals(loggedInCustomerId)) {
			logger.info("Can't create an Insurance Quote Request for a different customer.");
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		CustomerInfoEntity customerInfoEntity = customerInfoDto.toDomainObject();
		InsuranceOptionsEntity insuranceOptionsEntity = requestDto.getInsuranceOptions().toDomainObject();

		final Date date = new Date();
		InsuranceQuoteRequestAggregateRoot insuranceQuoteRequest = new InsuranceQuoteRequestAggregateRoot(date, RequestStatus.REQUEST_SUBMITTED, customerInfoEntity, insuranceOptionsEntity, null, null);
		insuranceQuoteRequestRepository.save(insuranceQuoteRequest);
		InsuranceQuoteRequestDto responseDto = InsuranceQuoteRequestDto.fromDomainObject(insuranceQuoteRequest);

		policyManagementMessageProducer.sendInsuranceQuoteRequest(date, responseDto);

		return ResponseEntity.ok(responseDto);
	}

	@Operation(summary = "Updates the status of an existing Insurance Quote Request")
	@PreAuthorize("isAuthenticated()")
	@PatchMapping(value = "/{id}") /* MAP: State Transition Operation */ 
	public ResponseEntity<InsuranceQuoteRequestDto> respondToInsuranceQuote(
			Authentication authentication,
			@Parameter(description = "the insurance quote request's unique id", required = true) @PathVariable Long id,
			@Parameter(description = "the response that contains the customer's decision whether to accept or reject an insurance quote", required = true)
			@Valid @RequestBody InsuranceQuoteResponseDto insuranceQuoteResponseDto) {
		String loggedInUserEmail = authentication.getName();
		UserLoginEntity loggedInUser = userLoginRepository.findByEmail(loggedInUserEmail);
		CustomerId loggedInCustomerId = loggedInUser.getCustomerId();
		if (loggedInCustomerId == null) {
			logger.info("Customer needs to complete registration first.");
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		Optional<InsuranceQuoteRequestAggregateRoot> optInsuranceQuoteRequest = insuranceQuoteRequestRepository.findById(id);
		if (!optInsuranceQuoteRequest.isPresent()) {
			final String errorMessage = "Failed to respond to insurance quote, because there is no insurance quote request with id '" + id + "'.";
			logger.info(errorMessage);
			throw new InsuranceQuoteRequestNotFoundException(errorMessage);
		}

		final InsuranceQuoteRequestAggregateRoot insuranceQuoteRequest = optInsuranceQuoteRequest.get();
		CustomerId customerId = insuranceQuoteRequest.getCustomerInfo().getCustomerId();
		if (!customerId.equals(loggedInCustomerId)) {
			logger.info("Can't update an Insurance Quote Request of a different customer.");
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		final Date date = new Date();
		if(insuranceQuoteResponseDto.getStatus().equals(RequestStatus.QUOTE_ACCEPTED.toString())) {
			logger.info("Insurance Quote has been accepted.");
			insuranceQuoteRequest.acceptQuote(date);
			policyManagementMessageProducer.sendCustomerDecision(date, insuranceQuoteRequest.getId(), true);
		} else if(insuranceQuoteResponseDto.getStatus().equals(RequestStatus.QUOTE_REJECTED.toString())) {
			logger.info("Insurance Quote has been rejected.");
			insuranceQuoteRequest.rejectQuote(date);
			policyManagementMessageProducer.sendCustomerDecision(date, insuranceQuoteRequest.getId(), false);
		}
		insuranceQuoteRequestRepository.save(insuranceQuoteRequest);

		InsuranceQuoteRequestDto insuranceQuoteRequestDto = InsuranceQuoteRequestDto.fromDomainObject(insuranceQuoteRequest);
		return ResponseEntity.ok(insuranceQuoteRequestDto);
	}
}
