package com.lakesidemutual.policymanagement.interfaces;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lakesidemutual.policymanagement.domain.customer.CustomerId;
import com.lakesidemutual.policymanagement.domain.policy.InsuringAgreementEntity;
import com.lakesidemutual.policymanagement.domain.policy.InsuringAgreementItem;
import com.lakesidemutual.policymanagement.domain.policy.MoneyAmount;
import com.lakesidemutual.policymanagement.domain.policy.PolicyAggregateRoot;
import com.lakesidemutual.policymanagement.domain.policy.PolicyId;
import com.lakesidemutual.policymanagement.domain.policy.PolicyPeriod;
import com.lakesidemutual.policymanagement.domain.policy.PolicyType;
import com.lakesidemutual.policymanagement.infrastructure.CustomerCoreService;
import com.lakesidemutual.policymanagement.infrastructure.PolicyRepository;
import com.lakesidemutual.policymanagement.infrastructure.RiskManagementService;
import com.lakesidemutual.policymanagement.interfaces.dtos.UnknownCustomerException;
import com.lakesidemutual.policymanagement.interfaces.dtos.customer.CustomerDto;
import com.lakesidemutual.policymanagement.interfaces.dtos.policy.CreatePolicyRequestDto;
import com.lakesidemutual.policymanagement.interfaces.dtos.policy.InsuringAgreementDto;
import com.lakesidemutual.policymanagement.interfaces.dtos.policy.InsuringAgreementItemDto;
import com.lakesidemutual.policymanagement.interfaces.dtos.policy.MoneyAmountDto;
import com.lakesidemutual.policymanagement.interfaces.dtos.policy.PaginatedPolicyResponseDto;
import com.lakesidemutual.policymanagement.interfaces.dtos.policy.PolicyDto;
import com.lakesidemutual.policymanagement.interfaces.dtos.policy.PolicyNotFoundException;
import com.lakesidemutual.policymanagement.interfaces.dtos.policy.PolicyPeriodDto;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * This REST controller gives clients access to the insurance policies. It is an example of the
 * <i>Information Holder Resource</i> pattern. This particular one is a special type of information holder called <i>Master Data Holder</i>.
 *
 * @see <a href="http://www.microservice-api-patterns.org/patterns/responsibility/endpointRoles/WADE-InformationHolderResource.html">Information Holder Resource</a>
 * @see <a href="http://www.microservice-api-patterns.org/patterns/responsibility/informationHolderEndpoints/WADE-MasterDataHolder.html">Master Data Holder</a>
 */
@RestController
@RequestMapping("/policies")
public class PolicyInformationHolder {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PolicyRepository policyRepository;

	@Autowired
	private RiskManagementService riskManagementService;

	@Autowired
	private CustomerCoreService customerCoreService;

	@ApiOperation(value = "Create a new policy.")
	@PostMapping
	public ResponseEntity<PolicyDto> createPolicy(
			@ApiParam(value = "the policy that is to be added", required = true)
			@Valid
			@RequestBody
			CreatePolicyRequestDto createPolicyDto,
			HttpServletRequest request) {

		String customerIdString = createPolicyDto.getCustomerId();
		CustomerId customerId = new CustomerId(customerIdString);
		List<CustomerDto> customers = customerCoreService.getCustomersById(customerId);
		if(customers.isEmpty()) {
			final String errorMessage = "Failed to find a customer with id '" + customerId.getId() + "'.";
			logger.info(errorMessage);
			throw new UnknownCustomerException(errorMessage);
		}

		PolicyId id = PolicyId.random();
		PolicyPeriodDto policyPeriodDto = createPolicyDto.getPolicyPeriod();
		PolicyType policyType = new PolicyType(createPolicyDto.getPolicyType());
		PolicyPeriod policyPeriod = new PolicyPeriod(policyPeriodDto.getStartDate(), policyPeriodDto.getEndDate());
		BigDecimal policyLimitAmount = createPolicyDto.getPolicyLimit().getAmount();
		Currency policyLimitCurrency = Currency.getInstance(createPolicyDto.getPolicyLimit().getCurrency());
		MoneyAmount policyLimit = new MoneyAmount(policyLimitAmount, policyLimitCurrency);
		BigDecimal insurancePremiumAmount = createPolicyDto.getInsurancePremium().getAmount();
		Currency insurancePremiumCurrency = Currency.getInstance(createPolicyDto.getInsurancePremium().getCurrency());
		MoneyAmount insurancePremium = new MoneyAmount(insurancePremiumAmount, insurancePremiumCurrency);
		List<InsuringAgreementItem> insuringAgreementItems = createPolicyDto.getInsuringAgreement().getAgreementItems().stream().map(item -> new InsuringAgreementItem(item.getTitle(), item.getDescription())).collect(Collectors.toList());
		InsuringAgreementEntity insuringAgreement = new InsuringAgreementEntity(insuringAgreementItems);
		PolicyAggregateRoot policy = new PolicyAggregateRoot(id, customerId, new Date(), policyPeriod, policyType, policyLimit, insurancePremium, insuringAgreement);
		policyRepository.save(policy);

		CustomerDto customer = customers.get(0);
		PolicyDto policyDto = createPolicyDtos(Arrays.asList(policy), "").get(0);
		riskManagementService.notifyRiskManagement(request.getRemoteAddr(), new Date(), customer, policyDto);
		return ResponseEntity.ok(policyDto);
	}

	private List<PolicyDto> createPolicyDtos(List<PolicyAggregateRoot> policies, String expand) {
		List<CustomerDto> customers = null;
		if(expand.equals("customer")) {
			List<CustomerId> customerIds = policies.stream().map(p -> p.getCustomerId()).collect(Collectors.toList());
			customers = customerCoreService.getCustomersById(customerIds.toArray(new CustomerId[customerIds.size()]));
		}

		List<PolicyDto> policyDtos = new ArrayList<>();
		for(int i = 0; i < policies.size(); i++) {
			PolicyAggregateRoot policy = policies.get(i);
			Object customer;
			if(customers != null) {
				customer = customers.get(i);
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

	@ApiOperation(value = "Get all policies, newest first.")
	@GetMapping
	public ResponseEntity<PaginatedPolicyResponseDto> getPolicies(
			@ApiParam(value = "the maximum number of policies per page", required = false) @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
			@ApiParam(value = "the offset of the page's first policy", required = false) @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
			@ApiParam(value = "a comma-separated list of the fields that should be expanded in the response", required = false) @RequestParam(value = "expand", required = false, defaultValue = "") String expand) {
		List<PolicyAggregateRoot> allPolicies = policyRepository.findAll(Sort.by(Sort.Direction.DESC, PolicyAggregateRoot.FIELD_CREATION_DATE));
		List<PolicyAggregateRoot> policies = allPolicies.stream().skip(offset).limit(limit).collect(Collectors.toList());
		List<PolicyDto> policyDtos = createPolicyDtos(policies, expand);
		PaginatedPolicyResponseDto paginatedPolicyResponse = createPaginatedPolicyResponseDto(limit, offset, expand, allPolicies.size(), policyDtos);
		return ResponseEntity.ok(paginatedPolicyResponse);
	}

	/**
	 * Returns the policy for the given policy id.
	 * <br><br>
	 * The query parameter {@code expand } allows clients to provide a so-called <a href="http://www.microservice-api-patterns.org/patterns/quality/WADE-WishList.html">Wish List</a>.
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
	 *      "http://www.microservice-api-patterns.org/patterns/quality/WADE-WishList.html">www.microservice-api-patterns.org/patterns/quality/WADE-WishList.html</a>
	 */
	@ApiOperation(value = "Get a single policy.")
	@GetMapping(value = "/{policyId}")
	public ResponseEntity<PolicyDto> getPolicy(
			@ApiParam(value = "the policy's unique id", required = true) @PathVariable PolicyId policyId,
			@ApiParam(value = "a comma-separated list of the fields that should be expanded in the response", required = false) @RequestParam(value = "expand", required = false, defaultValue = "") String expand) {

		Optional<PolicyAggregateRoot> optPolicy = policyRepository.findById(policyId);
		if(!optPolicy.isPresent()) {
			final String errorMessage = "Failed to find a policy with id '" + policyId.getId() + "'.";
			logger.info(errorMessage);
			throw new PolicyNotFoundException(errorMessage);
		}

		PolicyAggregateRoot policy = optPolicy.get();
		PolicyDto response = createPolicyDtos(Arrays.asList(policy), expand).get(0);
		return ResponseEntity.ok(response);
	}
}
