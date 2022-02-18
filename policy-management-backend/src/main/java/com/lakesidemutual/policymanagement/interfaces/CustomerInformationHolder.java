package com.lakesidemutual.policymanagement.interfaces;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lakesidemutual.policymanagement.domain.customer.CustomerId;
import com.lakesidemutual.policymanagement.domain.policy.PolicyAggregateRoot;
import com.lakesidemutual.policymanagement.infrastructure.CustomerCoreRemoteProxy;
import com.lakesidemutual.policymanagement.infrastructure.PolicyRepository;
import com.lakesidemutual.policymanagement.interfaces.dtos.customer.CustomerDto;
import com.lakesidemutual.policymanagement.interfaces.dtos.customer.CustomerIdDto;
import com.lakesidemutual.policymanagement.interfaces.dtos.customer.CustomerNotFoundException;
import com.lakesidemutual.policymanagement.interfaces.dtos.customer.PaginatedCustomerResponseDto;
import com.lakesidemutual.policymanagement.interfaces.dtos.policy.PolicyDto;

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
	private PolicyRepository policyRepository;

	@Autowired
	private CustomerCoreRemoteProxy customerCoreRemoteProxy;

	@Operation(summary = "Get all customers.")
	@GetMapping
	public ResponseEntity<PaginatedCustomerResponseDto> getCustomers(
			@Parameter(description = "search terms to filter the customers by name", required = false) @RequestParam(value = "filter", required = false, defaultValue = "") String filter,
			@Parameter(description = "the maximum number of customers per page", required = false) @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
			@Parameter(description = "the offset of the page's first customer", required = false) @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset) {
		logger.debug("Fetching a page of customers (offset={},limit={},filter='{}')", offset, limit, filter);
		PaginatedCustomerResponseDto paginatedResponseIn = customerCoreRemoteProxy.getCustomers(filter, limit, offset);
		PaginatedCustomerResponseDto paginatedResponseOut = createPaginatedCustomerResponseDto(
				paginatedResponseIn.getFilter(),
				paginatedResponseIn.getLimit(),
				paginatedResponseIn.getOffset(),
				paginatedResponseIn.getSize(),
				paginatedResponseIn.getCustomers());
		return ResponseEntity.ok(paginatedResponseOut);
	}

	private PaginatedCustomerResponseDto createPaginatedCustomerResponseDto(String filter, Integer limit, Integer offset, int size, List<CustomerDto> customerDtos) {
		customerDtos.forEach(this::addCustomerLinks);
		PaginatedCustomerResponseDto paginatedCustomerResponseDto = new PaginatedCustomerResponseDto(filter, limit, offset, size, customerDtos);
		paginatedCustomerResponseDto.add(linkTo(methodOn(CustomerInformationHolder.class).getCustomers(filter, limit, offset)).withSelfRel());

		if (offset > 0) {
			paginatedCustomerResponseDto.add(linkTo(
					methodOn(CustomerInformationHolder.class).getCustomers(filter, limit, Math.max(0, offset - limit)))
					.withRel("prev"));
		}

		if (offset < size - limit) {
			paginatedCustomerResponseDto.add(linkTo(methodOn(CustomerInformationHolder.class).getCustomers(filter, limit, offset + limit))
					.withRel("next"));
		}

		return paginatedCustomerResponseDto;
	}

	/**
	 * The CustomerDto could contain a nested list containing the customer's policies. However, many clients may not be
	 * interested in the policies when they access the customer resource. To avoid sending large messages containing lots
	 * of data that is not or seldom needed we instead add a link to a separate endpoint which returns the customer's policies.
	 * This is an example of the <i>Linked Information Holder</i> pattern.
	 *
	 * @see <a href="https://www.microservice-api-patterns.org/patterns/quality/referenceManagement/LinkedInformationHolder">Linked Information Holder</a>
	 */
	private void addCustomerLinks(CustomerDto customerDto) {
		CustomerIdDto customerId = new CustomerIdDto(customerDto.getCustomerId());
		Link selfLink = linkTo(methodOn(CustomerInformationHolder.class).getCustomer(customerId)).withSelfRel();
		Link policiesLink = linkTo(methodOn(CustomerInformationHolder.class).getPolicies(customerId, "")).withRel("policies");
		customerDto.add(selfLink);
		customerDto.add(policiesLink);
	}

	/**
	 * Returns the customer with the given customer id. Example Usage:
	 *
	 * <pre>
	 * <code>
	 * GET http://localhost:8090/customers/rgpp0wkpec
	 *
	 * {
	 *   "customerId" : "rgpp0wkpec",
	 *   "firstname" : "Max",
	 *   "lastname" : "Mustermann",
	 *   "birthday" : "1989-12-31T23:00:00.000+0000",
	 *   "streetAddress" : "Oberseestrasse 10",
	 *   "postalCode" : "8640",
	 *   "city" : "Rapperswil",
	 *   "email" : "admin@example.com",
	 *   "phoneNumber" : "055 222 4111",
	 *   "moveHistory" : [ ]
	 * }
	 * </code>
	 * </pre>
	 * If the given customer id is not valid, an error response with HTTP Status Code 404 is returned. The response body contains additional
	 * information about the error in JSON form. This is an example of the <a href="https://www.microservice-api-patterns.org/patterns/quality/qualityManagementAndGovernance/ErrorReport">Error Report</a>
	 * pattern:
	 * <pre>
	 * <code>
	 * GET http://localhost:8090/customers/123
	 *
	 * {
	 *   "timestamp" : "2018-09-18T08:28:44.644+0000",
	 *   "status" : 404,
	 *   "error" : "Not Found",
	 *   "message" : "Failed to find a customer with id '123'.",
	 *   "path" : "/customers/123"
	 * }
	 * </code>
	 * </pre>
	 *
	 * @see <a href="https://www.microservice-api-patterns.org/patterns/quality/qualityManagementAndGovernance/ErrorReport">https://www.microservice-api-patterns.org/patterns/quality/qualityManagementAndGovernance/ErrorReport</a>
	 */
	@Operation(summary = "Get customer with a given customer id.")
	@GetMapping(value = "/{customerIdDto}")
	public ResponseEntity<CustomerDto> getCustomer(
			@Parameter(description = "the customer's unique id", required = true) @PathVariable CustomerIdDto customerIdDto) {
		CustomerId customerId = new CustomerId(customerIdDto.getId());
		logger.debug("Fetching a customer with id '{}'", customerId.getId());
		CustomerDto customer = customerCoreRemoteProxy.getCustomer(customerId);
		if(customer == null) {
			final String errorMessage = "Failed to find a customer with id '{}'";
			logger.warn(errorMessage, customerId.getId());
			throw new CustomerNotFoundException(errorMessage);
		}

		addCustomerLinks(customer);
		return ResponseEntity.ok(customer);
	}

	@Operation(summary = "Get a customer's policies.")
	@GetMapping(value = "/{customerIdDto}/policies")
	public ResponseEntity<List<PolicyDto>> getPolicies(
			@Parameter(description = "the customer's unique id", required = true) @PathVariable CustomerIdDto customerIdDto,
			@Parameter(description = "a comma-separated list of the fields that should be expanded in the response", required = false) @RequestParam(value = "expand", required = false, defaultValue = "") String expand) {
		CustomerId customerId = new CustomerId(customerIdDto.getId());
		logger.debug("Fetching policies for customer with id '{}' (fields='{}')", customerId.getId(), expand);
		List<PolicyAggregateRoot> policies = policyRepository.findAllByCustomerIdOrderByCreationDateDesc(customerId);
		List<PolicyDto> policyDtos = policies.stream().map(p -> createPolicyDto(p, expand)).collect(Collectors.toList());
		return ResponseEntity.ok(policyDtos);
	}

	private PolicyDto createPolicyDto(PolicyAggregateRoot policy, String expand) {
		PolicyDto policyDto = PolicyDto.fromDomainObject(policy);
		if(expand.equals("customer")) {
			CustomerDto customer = customerCoreRemoteProxy.getCustomer(policy.getCustomerId());
			policyDto.setCustomer(customer);
		}

		Link selfLink = linkTo(methodOn(PolicyInformationHolder.class).getPolicy(policy.getId(), expand)).withSelfRel();
		policyDto.add(selfLink);
		return policyDto;
	}
}
