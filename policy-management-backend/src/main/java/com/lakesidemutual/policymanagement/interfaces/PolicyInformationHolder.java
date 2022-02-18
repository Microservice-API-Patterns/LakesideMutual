package com.lakesidemutual.policymanagement.interfaces;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lakesidemutual.policymanagement.domain.customer.CustomerId;
import com.lakesidemutual.policymanagement.domain.policy.DeletePolicyEvent;
import com.lakesidemutual.policymanagement.domain.policy.InsuringAgreementEntity;
import com.lakesidemutual.policymanagement.domain.policy.MoneyAmount;
import com.lakesidemutual.policymanagement.domain.policy.PolicyAggregateRoot;
import com.lakesidemutual.policymanagement.domain.policy.PolicyId;
import com.lakesidemutual.policymanagement.domain.policy.PolicyPeriod;
import com.lakesidemutual.policymanagement.domain.policy.PolicyType;
import com.lakesidemutual.policymanagement.domain.policy.UpdatePolicyEvent;
import com.lakesidemutual.policymanagement.infrastructure.CustomerCoreRemoteProxy;
import com.lakesidemutual.policymanagement.infrastructure.PolicyRepository;
import com.lakesidemutual.policymanagement.infrastructure.RiskManagementMessageProducer;
import com.lakesidemutual.policymanagement.interfaces.dtos.UnknownCustomerException;
import com.lakesidemutual.policymanagement.interfaces.dtos.customer.CustomerDto;
import com.lakesidemutual.policymanagement.interfaces.dtos.policy.CreatePolicyRequestDto;
import com.lakesidemutual.policymanagement.interfaces.dtos.policy.PaginatedPolicyResponseDto;
import com.lakesidemutual.policymanagement.interfaces.dtos.policy.PolicyDto;
import com.lakesidemutual.policymanagement.interfaces.dtos.policy.PolicyNotFoundException;

/**
 * This REST controller gives clients access to the insurance policies. It is an example of the
 * <i>Information Holder Resource</i> pattern. This particular one is a special type of information holder called <i>Master Data Holder</i>.
 *
 * @see <a href="https://www.microservice-api-patterns.org/patterns/responsibility/endpointRoles/InformationHolderResource">Information Holder Resource</a>
 * @see <a href="https://www.microservice-api-patterns.org/patterns/responsibility/informationHolderEndpointTypes/MasterDataHolder">Master Data Holder</a>
 */
@RestController
@RequestMapping("/policies")
public class PolicyInformationHolder {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PolicyRepository policyRepository;

	@Autowired
	private RiskManagementMessageProducer riskManagementMessageProducer;

	@Autowired
	private CustomerCoreRemoteProxy customerCoreRemoteProxy;

	@Operation(summary = "Create a new policy.")
	@PostMapping
	public ResponseEntity<PolicyDto> createPolicy(
			@Parameter(description = "the policy that is to be added", required = true)
			@Valid
			@RequestBody
			CreatePolicyRequestDto createPolicyDto,
			HttpServletRequest request) {
		String customerIdString = createPolicyDto.getCustomerId();
		logger.info("Creating a new policy for customer with id '{}'", customerIdString);
		CustomerId customerId = new CustomerId(customerIdString);
		List<CustomerDto> customers = customerCoreRemoteProxy.getCustomersById(customerId);
		if(customers.isEmpty()) {
			final String errorMessage = "Failed to find a customer with id '{}'";
			logger.warn(errorMessage, customerId.getId());
			throw new UnknownCustomerException(errorMessage);
		}

		PolicyId id = PolicyId.random();
		PolicyType policyType = new PolicyType(createPolicyDto.getPolicyType());
		PolicyPeriod policyPeriod = createPolicyDto.getPolicyPeriod().toDomainObject();
		MoneyAmount deductible = createPolicyDto.getDeductible().toDomainObject();
		MoneyAmount policyLimit = createPolicyDto.getPolicyLimit().toDomainObject();
		MoneyAmount insurancePremium = createPolicyDto.getInsurancePremium().toDomainObject();
		InsuringAgreementEntity insuringAgreement = createPolicyDto.getInsuringAgreement().toDomainObject();
		PolicyAggregateRoot policy = new PolicyAggregateRoot(id, customerId, new Date(), policyPeriod, policyType, deductible, policyLimit, insurancePremium, insuringAgreement);
		policyRepository.save(policy);

		CustomerDto customer = customers.get(0);
		PolicyDto policyDto = createPolicyDtos(Arrays.asList(policy), "").get(0);
		final UpdatePolicyEvent event = new UpdatePolicyEvent(request.getRemoteAddr(), new Date(), customer, policyDto);
		riskManagementMessageProducer.emitEvent(event);
		return ResponseEntity.ok(policyDto);
	}

	@Operation(summary = "Update an existing policy.")
	@PutMapping(value = "/{policyId}")
	public ResponseEntity<PolicyDto> updatePolicy(
			@Parameter(description = "the policy's unique id", required = true) @PathVariable PolicyId policyId,
			@Parameter(description = "the updated policy", required = true) @Valid @RequestBody CreatePolicyRequestDto createPolicyDto,
			HttpServletRequest request) {
		logger.info("Updating policy with id '{}'", policyId.getId());

		Optional<PolicyAggregateRoot> optPolicy = policyRepository.findById(policyId);
		if(!optPolicy.isPresent()) {
			final String errorMessage = "Failed to find a policy with id '{}'";
			logger.warn(errorMessage, policyId.getId());
			throw new PolicyNotFoundException(errorMessage);
		}

		CustomerId customerId = new CustomerId(createPolicyDto.getCustomerId());
		List<CustomerDto> customers = customerCoreRemoteProxy.getCustomersById(customerId);
		if(customers.isEmpty()) {
			final String errorMessage = "Failed to find a customer with id '{}'";
			logger.warn(errorMessage, customerId.getId());
			throw new UnknownCustomerException(errorMessage);
		}

		PolicyType policyType = new PolicyType(createPolicyDto.getPolicyType());
		PolicyPeriod policyPeriod = createPolicyDto.getPolicyPeriod().toDomainObject();
		MoneyAmount deductible = createPolicyDto.getDeductible().toDomainObject();
		MoneyAmount policyLimit = createPolicyDto.getPolicyLimit().toDomainObject();
		MoneyAmount insurancePremium = createPolicyDto.getInsurancePremium().toDomainObject();
		InsuringAgreementEntity insuringAgreement = createPolicyDto.getInsuringAgreement().toDomainObject();

		PolicyAggregateRoot policy = optPolicy.get();
		policy.setPolicyPeriod(policyPeriod);
		policy.setPolicyType(policyType);
		policy.setDeductible(deductible);
		policy.setPolicyLimit(policyLimit);
		policy.setInsurancePremium(insurancePremium);
		policy.setInsuringAgreement(insuringAgreement);
		policyRepository.save(policy);

		CustomerDto customer = customers.get(0);
		PolicyDto policyDto = createPolicyDtos(Arrays.asList(policy), "").get(0);
		final UpdatePolicyEvent event = new UpdatePolicyEvent(request.getRemoteAddr(), new Date(), customer, policyDto);
		riskManagementMessageProducer.emitEvent(event);

		PolicyDto response = createPolicyDtos(Arrays.asList(policy), "").get(0);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "Delete an existing policy.")
	@DeleteMapping(value = "/{policyId}")
	public ResponseEntity<Void> deletePolicy(
			@Parameter(description = "the policy's unique id", required = true) @PathVariable PolicyId policyId,
			HttpServletRequest request) {
		logger.info("Deleting policy with id '{}'", policyId.getId());
		policyRepository.deleteById(policyId);

		final DeletePolicyEvent event = new DeletePolicyEvent(request.getRemoteAddr(), new Date(), policyId.getId());
		riskManagementMessageProducer.emitEvent(event);

		return ResponseEntity.noContent().build();
	}

	private List<PolicyDto> createPolicyDtos(List<PolicyAggregateRoot> policies, String expand) {
		List<CustomerDto> customers = null;
		if(expand.equals("customer")) {
			List<CustomerId> customerIds = policies.stream().map(p -> p.getCustomerId()).collect(Collectors.toList());
			customers = customerCoreRemoteProxy.getCustomersById(customerIds.toArray(new CustomerId[customerIds.size()]));
		}

		List<PolicyDto> policyDtos = new ArrayList<>();
		for(int i = 0; i < policies.size(); i++) {
			PolicyAggregateRoot policy = policies.get(i);
			PolicyDto policyDto = PolicyDto.fromDomainObject(policy);
			if(customers != null) {
				CustomerDto customer = customers.get(i);
				policyDto.setCustomer(customer);
			}
			policyDtos.add(policyDto);
		}
		return policyDtos;
	}

	private PaginatedPolicyResponseDto createPaginatedPolicyResponseDto(Integer limit, Integer offset, String expand, int size,
			List<PolicyDto> policyDtos) {

		PaginatedPolicyResponseDto paginatedPolicyResponseDto = new PaginatedPolicyResponseDto(limit, offset,
				size, policyDtos);

		paginatedPolicyResponseDto.add(linkTo(methodOn(PolicyInformationHolder.class).getPolicies(limit, offset, expand)).withSelfRel());

		if (offset > 0) {
			paginatedPolicyResponseDto.add(linkTo(
					methodOn(PolicyInformationHolder.class).getPolicies(limit, Math.max(0, offset - limit), expand))
					.withRel("prev"));
		}

		if (offset < size - limit) {
			paginatedPolicyResponseDto.add(linkTo(methodOn(PolicyInformationHolder.class).getPolicies(limit, offset + limit, expand))
					.withRel("next"));
		}

		return paginatedPolicyResponseDto;
	}

	@Operation(summary = "Get all policies, newest first.")
	@GetMapping
	public ResponseEntity<PaginatedPolicyResponseDto> getPolicies(
			@Parameter(description = "the maximum number of policies per page", required = false) @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
			@Parameter(description = "the offset of the page's first policy", required = false) @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
			@Parameter(description = "a comma-separated list of the fields that should be expanded in the response", required = false) @RequestParam(value = "expand", required = false, defaultValue = "") String expand) {
		logger.debug("Fetching a page of policies (offset={},limit={},fields='{}')", offset, limit, expand);
		List<PolicyAggregateRoot> allPolicies = policyRepository.findAll(Sort.by(Sort.Direction.DESC, PolicyAggregateRoot.FIELD_CREATION_DATE));
		List<PolicyAggregateRoot> policies = allPolicies.stream().skip(offset).limit(limit).collect(Collectors.toList());
		List<PolicyDto> policyDtos = createPolicyDtos(policies, expand);
		PaginatedPolicyResponseDto paginatedPolicyResponse = createPaginatedPolicyResponseDto(limit, offset, expand, allPolicies.size(), policyDtos);
		return ResponseEntity.ok(paginatedPolicyResponse);
	}

	/**
	 * Returns the policy for the given policy id.
	 * <br><br>
	 * The query parameter {@code expand } allows clients to provide a so-called <a href="https://www.microservice-api-patterns.org/patterns/quality/dataTransferParsimony/WishList">Wish List</a>.
	 * By default, getPolicy() returns the following response:
	 * <pre>
	 *   <code>
	 * GET http://localhost:8090/policies/h3riovf4xq/
	 *
	 * {
	 *   "_expandable" : [ "customer" ],
	 *   "policyId" : "h3riovf4xq",
	 *   "customer" : "rgpp0wkpec",
	 *   "creationDate" : "2018-06-19T12:45:46.743+0000",
	 *   ...
	 * }
	 *   </code>
	 * </pre>
	 *
	 * The response includes only the customer's id. The {@code _expandable } section indicates that the {@code customer } resource can be expanded:
	 *
	 * <pre>
	 *   <code>
	 * GET http://localhost:8090/policies/h3riovf4xq/?expand=customer
	 *
	 * {
	 *   "_expandable" : [ "customer" ],
	 *   "policyId" : "h3riovf4xq",
	 *   "customer" : {
	 *     "customerId" : "rgpp0wkpec",
	 *     "customerProfile" : {
	 *       "firstname" : "Max",
	 *       "lastname" : "Mustermann",
	 *       ...
	 *     },
	 *     ...
	 *   },
	 *   "creationDate" : "2018-06-19T12:45:46.743+0000",
	 *   ...
	 * }
	 *   </code>
	 * </pre>
	 *
	 * @see <a href=
	 *      "https://www.microservice-api-patterns.org/patterns/quality/dataTransferParsimony/WishList">https://www.microservice-api-patterns.org/patterns/quality/dataTransferParsimony/WishList</a>
	 */
	@Operation(summary = "Get a single policy.")
	@GetMapping(value = "/{policyId}")
	public ResponseEntity<PolicyDto> getPolicy(
			@Parameter(description = "the policy's unique id", required = true) @PathVariable PolicyId policyId,
			@Parameter(description = "a comma-separated list of the fields that should be expanded in the response", required = false) @RequestParam(value = "expand", required = false, defaultValue = "") String expand) {
		logger.debug("Fetching policy with id '{}'", policyId.getId());
		Optional<PolicyAggregateRoot> optPolicy = policyRepository.findById(policyId);
		if(!optPolicy.isPresent()) {
			final String errorMessage = "Failed to find a policy with id '{}'";
			logger.warn(errorMessage, policyId.getId());
			throw new PolicyNotFoundException(errorMessage);
		}

		PolicyAggregateRoot policy = optPolicy.get();
		PolicyDto response = createPolicyDtos(Arrays.asList(policy), expand).get(0);
		return ResponseEntity.ok(response);
	}
}
