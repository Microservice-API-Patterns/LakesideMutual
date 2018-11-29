package com.lakesidemutual.policymanagement.interfaces;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
import com.lakesidemutual.policymanagement.domain.policy.InsuringAgreementEntity;
import com.lakesidemutual.policymanagement.domain.policy.MoneyAmount;
import com.lakesidemutual.policymanagement.domain.policy.PolicyAggregateRoot;
import com.lakesidemutual.policymanagement.domain.policy.PolicyPeriod;
import com.lakesidemutual.policymanagement.infrastructure.CustomerCoreService;
import com.lakesidemutual.policymanagement.infrastructure.PolicyRepository;
import com.lakesidemutual.policymanagement.interfaces.dtos.customer.CustomerDto;
import com.lakesidemutual.policymanagement.interfaces.dtos.customer.CustomerIdDto;
import com.lakesidemutual.policymanagement.interfaces.dtos.customer.CustomerNotFoundException;
import com.lakesidemutual.policymanagement.interfaces.dtos.customer.PaginatedCustomerResponseDto;
import com.lakesidemutual.policymanagement.interfaces.dtos.policy.InsuringAgreementDto;
import com.lakesidemutual.policymanagement.interfaces.dtos.policy.InsuringAgreementItemDto;
import com.lakesidemutual.policymanagement.interfaces.dtos.policy.MoneyAmountDto;
import com.lakesidemutual.policymanagement.interfaces.dtos.policy.PolicyDto;
import com.lakesidemutual.policymanagement.interfaces.dtos.policy.PolicyNotFoundException;
import com.lakesidemutual.policymanagement.interfaces.dtos.policy.PolicyPeriodDto;

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
	private PolicyRepository policyRepository;

	@Autowired
	private CustomerCoreService customerCoreService;

	@ApiOperation(value = "Get all customers.")
	@GetMapping
	public ResponseEntity<PaginatedCustomerResponseDto> getCustomers(
			@ApiParam(value = "search terms to filter the customers by name", required = false) @RequestParam(value = "filter", required = false, defaultValue = "") String filter,
			@ApiParam(value = "the maximum number of customers per page", required = false) @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
			@ApiParam(value = "the offset of the page's first customer", required = false) @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset) {
		PaginatedCustomerResponseDto paginatedResponseIn = customerCoreService.getCustomers(filter, limit, offset);
		PaginatedCustomerResponseDto paginatedResponseOut = createPaginatedCustomerResponseDto(
				paginatedResponseIn.getFilter(),
				paginatedResponseIn.getLimit(),
				paginatedResponseIn.getOffset(),
				paginatedResponseIn.getSize(),
				paginatedResponseIn.getCustomers());
		return ResponseEntity.ok(paginatedResponseOut);
	}

	private PaginatedCustomerResponseDto createPaginatedCustomerResponseDto(String filter, Integer limit, Integer offset, int size, List<CustomerDto> customerDtos) {
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
	 * information about the error in JSON form. This is an example of the <a href="http://www.microservice-api-patterns.org/patterns/quality/qualityManagementAndGovernance/WADE-ErrorReporting.html">Error Reporting</a>
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
	 * @see <a href="http://www.microservice-api-patterns.org/patterns/quality/qualityManagementAndGovernance/WADE-ErrorReporting.html">www.microservice-api-patterns.org/patterns/quality/qualityManagementAndGovernance/WADE-ErrorReporting.html</a>
	 */
	@ApiOperation(value = "Get customer with a given customer id.")
	@GetMapping(value = "/{customerIdDto}")
	public ResponseEntity<CustomerDto> getCustomer(
			@ApiParam(value = "the customer's unique id", required = true) @PathVariable CustomerIdDto customerIdDto) {
		CustomerId customerId = new CustomerId(customerIdDto.getId());
		CustomerDto customer = customerCoreService.getCustomer(customerId);
		if(customer == null) {
			final String errorMessage = "Failed to find a customer with id '" + customerId.getId() + "'.";
			logger.info(errorMessage);
			throw new CustomerNotFoundException(errorMessage);
		}
		return ResponseEntity.ok(customer);
	}

	@ApiOperation(value = "Get a customer's policy history.")
	@GetMapping(value = "/{customerIdDto}/policy-history")
	public ResponseEntity<List<PolicyDto>> getPolicyHistory(
			@ApiParam(value = "the customer's unique id", required = true) @PathVariable CustomerIdDto customerIdDto,
			@ApiParam(value = "a comma-separated list of the fields that should be expanded in the response", required = false) @RequestParam(value = "expand", required = false, defaultValue = "") String expand) {

		CustomerId customerId = new CustomerId(customerIdDto.getId());
		List<PolicyAggregateRoot> policies = policyRepository.findAllByCustomerIdOrderByCreationDateDesc(customerId);
		if(policies.size() > 0) {
			policies.remove(0);
		}
		List<PolicyDto> policyDtos = policies.stream().map(p -> createPolicyDto(p, expand)).collect(Collectors.toList());
		return ResponseEntity.ok(policyDtos);
	}

	@ApiOperation(value = "Get a customer's active policy.")
	@GetMapping(value = "/{customerIdDto}/active-policy")
	public ResponseEntity<PolicyDto> getActivePolicy(
			@ApiParam(value = "the customer's unique id", required = true) @PathVariable CustomerIdDto customerIdDto,
			@ApiParam(value = "a comma-separated list of the fields that should be expanded in the response", required = false) @RequestParam(value = "expand", required = false, defaultValue = "") String expand) {

		CustomerId customerId = new CustomerId(customerIdDto.getId());
		Optional<PolicyAggregateRoot> optPolicy = policyRepository.findFirstByCustomerIdOrderByCreationDateDesc(customerId);
		if(!optPolicy.isPresent()) {
			final String errorMessage = "Failed to find a policy for a customer with id '" + customerIdDto.getId() + "'.";
			logger.info(errorMessage);
			throw new PolicyNotFoundException(errorMessage);
		}

		PolicyAggregateRoot policy = optPolicy.get();
		PolicyDto policyDto = createPolicyDto(policy, expand);
		return ResponseEntity.ok(policyDto);
	}

	private PolicyDto createPolicyDto(PolicyAggregateRoot policy, String expand) {
		Object customer;
		if(expand.equals("customer")) {
			customer = customerCoreService.getCustomer(policy.getCustomerId());
		} else {
			customer = policy.getCustomerId().getId();
		}

		PolicyPeriod policyPeriod = policy.getPolicyPeriod();
		PolicyPeriodDto policyPeriodDto = new PolicyPeriodDto(policyPeriod.getStartDate(), policyPeriod.getEndDate());
		InsuringAgreementEntity insuringAgreement = policy.getInsuringAgreement();
		List<InsuringAgreementItemDto> insuringAgreementItemDtos = insuringAgreement.getAgreementItems().stream().map(item -> new InsuringAgreementItemDto(item.getTitle(), item.getDescription())).collect(Collectors.toList());
		InsuringAgreementDto insuringAgreementDto = new InsuringAgreementDto(insuringAgreementItemDtos);
		MoneyAmount policyLimit = policy.getPolicyLimit();
		MoneyAmountDto policyLimitDto = new MoneyAmountDto(policyLimit.getAmount(), policyLimit.getCurrency().toString());
		MoneyAmount insurancePremium = policy.getInsurancePremium();
		MoneyAmountDto insurancePremiumDto = new MoneyAmountDto(insurancePremium.getAmount(), insurancePremium.getCurrency().toString());

		PolicyDto policyDto = new PolicyDto(
				policy.getId().getId(),
				customer,
				policy.getCreationDate(),
				policyPeriodDto,
				policy.getPolicyType().getName(),
				policyLimitDto,
				insurancePremiumDto,
				insuringAgreementDto);

		Link selfLink = linkTo(methodOn(PolicyInformationHolder.class).getPolicy(policy.getId(), expand)).withSelfRel();
		policyDto.add(selfLink);
		return policyDto;
	}
}
