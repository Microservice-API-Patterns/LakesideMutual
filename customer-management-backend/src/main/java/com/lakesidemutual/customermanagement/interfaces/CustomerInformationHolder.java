package com.lakesidemutual.customermanagement.interfaces;

import javax.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lakesidemutual.customermanagement.domain.customer.CustomerId;
import com.lakesidemutual.customermanagement.infrastructure.CustomerCoreRemoteProxy;
import com.lakesidemutual.customermanagement.interfaces.dtos.CustomerDto;
import com.lakesidemutual.customermanagement.interfaces.dtos.CustomerNotFoundException;
import com.lakesidemutual.customermanagement.interfaces.dtos.CustomerProfileDto;
import com.lakesidemutual.customermanagement.interfaces.dtos.PaginatedCustomerResponseDto;

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
	private CustomerCoreRemoteProxy customerCoreRemoteProxy;

	@Operation(summary = "Get all customers.")
	@GetMapping
	public ResponseEntity<PaginatedCustomerResponseDto> getCustomers(
			@Parameter(description = "search terms to filter the customers by name", required = false) @RequestParam(value = "filter", required = false, defaultValue = "") String filter,
			@Parameter(description = "the maximum number of customers per page", required = false) @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
			@Parameter(description = "the offset of the page's first customer", required = false) @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset) {
		return ResponseEntity.ok(customerCoreRemoteProxy.getCustomers(filter, limit, offset));
	}

	@Operation(summary = "Get customer with a given customer id.")
	@GetMapping(value = "/{customerId}")
	public ResponseEntity<CustomerDto> getCustomer(
			@Parameter(description = "the customer's unique id", required = true) @PathVariable CustomerId customerId) {

		CustomerDto customer = customerCoreRemoteProxy.getCustomer(customerId);
		if(customer == null) {
			final String errorMessage = "Failed to find a customer with id '" + customerId.getId() + "'.";
			logger.info(errorMessage);
			throw new CustomerNotFoundException(errorMessage);
		}
		return ResponseEntity.ok(customer);
	}

	@Operation(summary = "Update the profile of the customer with the given customer id")
	@PutMapping(value = "/{customerId}")
	public ResponseEntity<CustomerDto> updateCustomer(
			@Parameter(description = "the customer's unique id", required = true) @PathVariable CustomerId customerId,
			@Parameter(description = "the customer's updated profile", required = true) @Valid @RequestBody CustomerProfileDto customerProfile) {
		return customerCoreRemoteProxy.updateCustomer(customerId, customerProfile);
	}
}
