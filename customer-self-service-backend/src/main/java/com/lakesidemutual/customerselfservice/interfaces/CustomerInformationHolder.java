package com.lakesidemutual.customerselfservice.interfaces;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lakesidemutual.customerselfservice.domain.customer.CustomerId;
import com.lakesidemutual.customerselfservice.domain.identityaccess.UserLoginEntity;
import com.lakesidemutual.customerselfservice.domain.insurancequoterequest.InsuranceQuoteRequestAggregateRoot;
import com.lakesidemutual.customerselfservice.infrastructure.CustomerCoreRemoteProxy;
import com.lakesidemutual.customerselfservice.infrastructure.InsuranceQuoteRequestRepository;
import com.lakesidemutual.customerselfservice.infrastructure.UserLoginRepository;
import com.lakesidemutual.customerselfservice.interfaces.dtos.customer.AddressDto;
import com.lakesidemutual.customerselfservice.interfaces.dtos.customer.CustomerDto;
import com.lakesidemutual.customerselfservice.interfaces.dtos.customer.CustomerNotFoundException;
import com.lakesidemutual.customerselfservice.interfaces.dtos.customer.CustomerProfileUpdateRequestDto;
import com.lakesidemutual.customerselfservice.interfaces.dtos.customer.CustomerRegistrationRequestDto;
import com.lakesidemutual.customerselfservice.interfaces.dtos.insurancequoterequest.InsuranceQuoteRequestDto;

/**
 * This REST controller gives clients access to the customer data. It is an example of the
 * <i>Information Holder Resource</i> pattern. This particular one is a special type of information holder called <i>Master Data Holder</i>.
 *
 * @see <a href="https://www.microservice-api-patterns.org/patterns/responsibility/endpointRoles/InformationHolderResource">Information Holder Resource</a>
 * @see <a href="https://www.microservice-api-patterns.org/patterns/responsibility/informationHolderEndpointTypes/MasterDataHolder">Master Data Holder</a>
 */
@RestController
@RequestMapping("/customers")
public class CustomerInformationHolder {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserLoginRepository userLoginRepository;

	@Autowired
	private InsuranceQuoteRequestRepository insuranceQuoteRequestRepository;

	@Autowired
	private CustomerCoreRemoteProxy customerCoreRemoteProxy;

	@Operation(summary = "Change a customer's address.")
	@PreAuthorize("isAuthenticated()")
	@PutMapping(value = "/{customerId}/address")
	public ResponseEntity<AddressDto> changeAddress(
			@Parameter(description = "the customer's unique id", required = true) @PathVariable CustomerId customerId,
			@Parameter(description = "the customer's new address", required = true) @Valid @RequestBody AddressDto requestDto) {
		return customerCoreRemoteProxy.changeAddress(customerId, requestDto);
	}

	@Operation(summary = "Get customer with a given customer id.")
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/{customerId}")
	public ResponseEntity<CustomerDto> getCustomer(
			Authentication authentication,
			@Parameter(description = "the customer's unique id", required = true) @PathVariable CustomerId customerId) {

		CustomerDto customer = customerCoreRemoteProxy.getCustomer(customerId);
		if(customer == null) {
			final String errorMessage = "Failed to find a customer with id '" + customerId.getId() + "'.";
			logger.info(errorMessage);
			throw new CustomerNotFoundException(errorMessage);
		}

		addHATEOASLinks(customer);
		return ResponseEntity.ok(customer);
	}

	@Operation(summary = "Complete the registration of a new customer.")
	@PreAuthorize("isAuthenticated()")
	@PostMapping
	public ResponseEntity<CustomerDto> registerCustomer(
			Authentication authentication,
			@Parameter(description = "the customer's profile information", required = true) @Valid @RequestBody CustomerRegistrationRequestDto requestDto) {
		String loggedInUserEmail = authentication.getName();
		CustomerProfileUpdateRequestDto dto = new CustomerProfileUpdateRequestDto(
				requestDto.getFirstname(), requestDto.getLastname(), requestDto.getBirthday(), requestDto.getStreetAddress(),
				requestDto.getPostalCode(), requestDto.getCity(), loggedInUserEmail, requestDto.getPhoneNumber());
		CustomerDto customer = customerCoreRemoteProxy.createCustomer(dto);
		UserLoginEntity loggedInUser = userLoginRepository.findByEmail(loggedInUserEmail);
		loggedInUser.setCustomerId(new CustomerId(customer.getCustomerId()));
		userLoginRepository.save(loggedInUser);

		addHATEOASLinks(customer);
		return ResponseEntity.ok(customer);
	}

	@Operation(summary = "Get a customer's insurance quote requests.")
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/{customerId}/insurance-quote-requests")
	public ResponseEntity<List<InsuranceQuoteRequestDto>> getInsuranceQuoteRequests(
			@Parameter(description = "the customer's unique id", required = true) @PathVariable CustomerId customerId) {
		List<InsuranceQuoteRequestAggregateRoot> insuranceQuoteRequests = insuranceQuoteRequestRepository.findByCustomerInfo_CustomerIdOrderByDateDesc(customerId);
		List<InsuranceQuoteRequestDto> insuranceQuoteRequestDtos = insuranceQuoteRequests.stream().map(InsuranceQuoteRequestDto::fromDomainObject).collect(Collectors.toList());
		return ResponseEntity.ok(insuranceQuoteRequestDtos);
	}

	private void addHATEOASLinks(CustomerDto customerDto) {
		CustomerId customerId = new CustomerId(customerDto.getCustomerId());
		Link selfLink = linkTo(methodOn(CustomerInformationHolder.class).getCustomer(null, customerId))
				.withSelfRel();
		Link updateAddressLink = linkTo(methodOn(CustomerInformationHolder.class).changeAddress(customerId, null))
				.withRel("address.change");
		// When getting the DTO from the proxy, it already contains links
		// pointing to the customer-core, so we first remove them ...
		customerDto.removeLinks();
		// and add our own:
		customerDto.add(selfLink);
		customerDto.add(updateAddressLink);
	}
}
