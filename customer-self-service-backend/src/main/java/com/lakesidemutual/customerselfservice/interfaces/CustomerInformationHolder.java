package com.lakesidemutual.customerselfservice.interfaces;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lakesidemutual.customerselfservice.domain.customer.CustomerId;
import com.lakesidemutual.customerselfservice.domain.identityaccess.UserLogin;
import com.lakesidemutual.customerselfservice.infrastructure.CustomerCoreService;
import com.lakesidemutual.customerselfservice.infrastructure.UserLoginRepository;
import com.lakesidemutual.customerselfservice.interfaces.dtos.customer.AddressDto;
import com.lakesidemutual.customerselfservice.interfaces.dtos.customer.CustomerDto;
import com.lakesidemutual.customerselfservice.interfaces.dtos.customer.CustomerNotFoundException;
import com.lakesidemutual.customerselfservice.interfaces.dtos.customer.CustomerProfileUpdateRequestDto;
import com.lakesidemutual.customerselfservice.interfaces.dtos.customer.CustomerRegistrationRequestDto;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * This REST controller gives clients access to the customer data. It is an example of the
 * <i>Information Holder Resource</i> pattern. This particular one is a special type of information holder called <i>Master Data Holder</i>.
 *
 * @see <a href="http://www.microservice-api-patterns.org/patterns/responsibility/endpointRoles/WADE-InformationHolderResource.html">Information Holder Resource</a>
 * @see <a href="http://www.microservice-api-patterns.org/patterns/responsibility/informationHolderEndpoints/WADE-MasterDataHolder.html">Master Data Holder</a>
 */
@RestController
@RequestMapping("/customers")
public class CustomerInformationHolder {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserLoginRepository userLoginRepository;

	@Autowired
	private CustomerCoreService customerCoreService;

	@ApiOperation(value = "Change a customer's address.")
	@PutMapping(value = "/{customerId}/address")
	public ResponseEntity<AddressDto> changeAddress(
			@ApiParam(value = "the customer's unique id", required = true) @PathVariable CustomerId customerId,
			@ApiParam(value = "the customer's new address", required = true) @Valid @RequestBody AddressDto requestDto) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		return customerCoreService.changeAddress(customerId, requestDto);
	}

	@ApiOperation(value = "Get customer with a given customer id.")
	@GetMapping(value = "/{customerId}")
	public ResponseEntity<CustomerDto> getCustomer(
			@ApiParam(value = "the customer's unique id", required = true) @PathVariable CustomerId customerId) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		CustomerDto customer = customerCoreService.getCustomer(customerId);
		if(customer == null) {
			final String errorMessage = "Failed to find a customer with id '" + customerId.getId() + "'.";
			logger.info(errorMessage);
			throw new CustomerNotFoundException(errorMessage);
		}

		addHATEOASLinks(customer);
		return ResponseEntity.ok(customer);
	}

	@ApiOperation(value = "Complete the registration of a new customer.")
	@PostMapping
	public ResponseEntity<CustomerDto> registerCustomer(
			@ApiParam(value = "the customer's profile information", required = true) @Valid @RequestBody CustomerRegistrationRequestDto requestDto) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		String loggedInUserEmail = authentication.getName();
		CustomerProfileUpdateRequestDto dto = new CustomerProfileUpdateRequestDto(
				requestDto.getFirstname(), requestDto.getLastname(), requestDto.getBirthday(), requestDto.getStreetAddress(),
				requestDto.getPostalCode(), requestDto.getCity(), loggedInUserEmail, requestDto.getPhoneNumber());
		CustomerDto customer = customerCoreService.createCustomer(dto);
		UserLogin loggedInUser = userLoginRepository.findByEmail(loggedInUserEmail);
		loggedInUser.setCustomerId(new CustomerId(customer.getCustomerId()));
		userLoginRepository.save(loggedInUser);

		addHATEOASLinks(customer);
		return ResponseEntity.ok(customer);
	}

	private void addHATEOASLinks(CustomerDto customerDto) {
		CustomerId customerId = new CustomerId(customerDto.getCustomerId());
		Link selfLink = linkTo(methodOn(CustomerInformationHolder.class).getCustomer(customerId))
				.withSelfRel();
		Link updateAddressLink = linkTo(methodOn(CustomerInformationHolder.class).changeAddress(customerId, null))
				.withRel("address.change");
		customerDto.add(selfLink);
		customerDto.add(updateAddressLink);
	}
}
