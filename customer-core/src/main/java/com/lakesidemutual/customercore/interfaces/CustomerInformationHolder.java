package com.lakesidemutual.customercore.interfaces;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriUtils;

import com.lakesidemutual.customercore.application.CustomerService;
import com.lakesidemutual.customercore.application.Page;
import com.lakesidemutual.customercore.domain.customer.Address;
import com.lakesidemutual.customercore.domain.customer.CustomerAggregateRoot;
import com.lakesidemutual.customercore.domain.customer.CustomerId;
import com.lakesidemutual.customercore.domain.customer.CustomerProfileEntity;
import com.lakesidemutual.customercore.interfaces.dtos.customer.AddressDto;
import com.lakesidemutual.customercore.interfaces.dtos.customer.CustomerNotFoundException;
import com.lakesidemutual.customercore.interfaces.dtos.customer.CustomerProfileUpdateRequestDto;
import com.lakesidemutual.customercore.interfaces.dtos.customer.CustomerResponseDto;
import com.lakesidemutual.customercore.interfaces.dtos.customer.CustomersResponseDto;
import com.lakesidemutual.customercore.interfaces.dtos.customer.PaginatedCustomerResponseDto;

/**
 * This REST controller gives clients access to the customer data. It is an example of the
 * <i>Information Holder Resource</i> pattern. This particular one is a special type of information holder called <i>Master Data Holder</i>.
 *
 * @see <a href="https://www.microservice-api-patterns.org/patterns/responsibility/endpointRoles/InformationHolderResource">Information Holder Resource</a>
 * @see <a href="https://www.microservice-api-patterns.org/patterns/responsibility/informationHolderEndpointTypes/MasterDataHolder">Master Data Holder</a>
 *
 * Note: JAX-WS and JAX-RS (or Spring Web Services and REST annotations) support could be added to the same controller/port class (in port-and-adapter or hexagon style terms, by A. Cockburn). 
 * But such hybrid approach gets messy soon (due to "annotation jungle"). Hence, there is a separate CustomerInformationHolderSOAPAdapter.
 *
 */

@RestController
@RequestMapping("/customers")
public class CustomerInformationHolder {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * Attributes marked with @Autowired are automatically created and injected by
	 * Spring.
	 */
	@Autowired
	private CustomerService customerService;
	
	private Set<String> getIncludedFields(String fields) {
		if (fields.trim().isEmpty()) {
			return Collections.emptySet();
		} else {
			return new HashSet<>(Arrays.asList(fields.split(",")));
		}
	}

	private CustomerResponseDto createCustomerResponseDto(CustomerAggregateRoot customer, String fields) {
		final Set<String> includedFields = getIncludedFields(fields);
		CustomerResponseDto customerResponseDto = new CustomerResponseDto(includedFields, customer);
		Link selfLink = linkTo(
				methodOn(CustomerInformationHolder.class).getCustomer(customer.getId().toString(), fields, "link"))
				.withSelfRel();

		Link updateAddressLink = linkTo(methodOn(CustomerInformationHolder.class).changeAddress(customer.getId(), null, "link"))
				.withRel("address.change");

		customerResponseDto.add(selfLink);
		customerResponseDto.add(updateAddressLink);
		return customerResponseDto;
	}

	private PaginatedCustomerResponseDto createPaginatedCustomerResponseDto(String filter, Integer limit,
			Integer offset, int size, String fields, List<CustomerResponseDto> customerDtos) {

		PaginatedCustomerResponseDto paginatedCustomerResponseDto = new PaginatedCustomerResponseDto(filter, limit,
				offset, size, customerDtos);

		paginatedCustomerResponseDto
		.add(linkTo(methodOn(CustomerInformationHolder.class).getCustomers(filter, limit, offset, fields, "link"))
				.withSelfRel());

		if (offset > 0) {
			paginatedCustomerResponseDto.add(linkTo(methodOn(CustomerInformationHolder.class).getCustomers(filter,
					limit, Math.max(0, offset - limit), fields, "link")).withRel("prev"));
		}

		if (offset < size - limit) {
			paginatedCustomerResponseDto.add(linkTo(
					methodOn(CustomerInformationHolder.class).getCustomers(filter, limit, offset + limit, fields, "link"))
					.withRel("next"));
		}

		return paginatedCustomerResponseDto;
	}
	

	/**
	 * Returns a 'page' of customers. <br>
	 * <br>
	 * The query parameters {@code limit} and {@code offset} can be used to specify
	 * the maximum size of the page and the offset of the page's first customer.
	 * Example Usage:
	 *
	 * <pre>
	 * <code>
	 * GET http://localhost:8110/customers?limit=2&offset=2
	 *
	 * {
	 *   "limit" : 2,
	 *   "offset" : 2,
	 *   "size" : 50,
	 *   "customers" : [ {
	 *     "customerId" : "ctkdzorjl0",
	 *     "firstname" : "Rahel",
	 *     "lastname" : "Piletic",
	 *     ...
	 *  }, {
	 *     "customerId" : "5xvivyzxvc",
	 *     "firstname" : "Cullin",
	 *     "lastname" : "Manske",
	 *     ...
	 *  } ],
	 *   "_links" : {
	 *     "self" : {
	 *       "href" : "http://localhost:8110/customers?limit=2&offset=2&fields="
	 *     },
	 *     "prev" : {
	 *       "href" : "http://localhost:8110/customers?limit=2&offset=0&fields="
	 *     },
	 *     "next" : {
	 *       "href" : "http://localhost:8110/customers?limit=2&offset=4&fields="
	 *     }
	 *   }
	 * }
	 * </code>
	 * </pre>
	 *
	 * The response contains the customers, limit and offset of the current page as
	 * well as the total number of customers (size). Additionally, it contains
	 * HATEOAS-style links that link to the endpoint address of the current,
	 * previous and next page.
	 *
	 * @see <a href=
	 *      "https://www.microservice-api-patterns.org/patterns/structure/compositeRepresentations/Pagination">https://www.microservice-api-patterns.org/patterns/structure/compositeRepresentations/Pagination</a>
	 */
	@Operation(summary = "Get all customers in pages of 10 entries per page.")
	@GetMapping // MAP operation responsibility: Retrieval Operation
	public ResponseEntity<PaginatedCustomerResponseDto> getCustomers(
			@Parameter(description = "search terms to filter the customers by name", required = false) @RequestParam(value = "filter", required = false, defaultValue = "") String filter,
			@Parameter(description = "the maximum number of customers per page", required = false) @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
			@Parameter(description = "the offset of the page's first customer", required = false) @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
			@Parameter(description = "a comma-separated list of the fields that should be included in the response", required = false) @RequestParam(value = "fields", required = false, defaultValue = "") String fields,
			@RequestHeader(value = "rest-tester", required = false) String restTester) {
		
		if(restTester == null){
			logger.info("TRIGGERED EDGE");
		} else {
			logger.info("TRIGGERED NODE: " + restTester);
		} 
		final String decodedFilter = UriUtils.decode(filter, "UTF-8");
		final Page<CustomerAggregateRoot> customerPage = customerService.getCustomers(decodedFilter, limit, offset);
		List<CustomerResponseDto> customerDtos = customerPage.getElements().stream().map(c -> createCustomerResponseDto(c, fields)).collect(Collectors.toList());

		PaginatedCustomerResponseDto paginatedCustomerResponseDto = createPaginatedCustomerResponseDto(
				filter,
				customerPage.getLimit(),
				customerPage.getOffset(),
				customerPage.getSize(),
				fields,
				customerDtos);
		return ResponseEntity.ok(paginatedCustomerResponseDto);
	}

	/**
	 * Returns the customers for the given customer ids. <br>
	 * <br>
	 * The client can provide a comma-separated list of customer ids to fetch a
	 * particular set of customers. This is a variant of the <a href=
	 * "https://www.microservice-api-patterns.org/patterns/quality/dataTransferParsimony/RequestBundle">Request
	 * Bundle</a> pattern:
	 *
	 * <pre>
	 *   <code>
	 * GET http://localhost:8110/customers/ce4btlyluu,rgpp0wkpec
	 *
	 * {
	 *   "customers" : [
	 *     {
	 *       "customerId" : "ce4btlyluu",
	 *       "firstname" : "Robbie",
	 *       "lastname" : "Davenhall",
	 *       "birthday" : "1961-08-11T23:00:00.000+0000",
	 *       ...
	 *       "_links" : { ... }
	 *     },
	 *     {
	 *       "customerId" : "rgpp0wkpec",
	 *       "firstname" : "Max",
	 *       "lastname" : "Mustermann",
	 *       "birthday" : "1989-12-31T23:00:00.000+0000",
	 *       ...
	 *       "_links" : { ... }
	 *     }
	 *   ],
	 *   "_links" : {
	 *     "self" : {
	 *       "href" : "http://localhost:8110/customers/ce4btlyluu,rgpp0wkpec?fields="
	 *     }
	 *   }
	 * }
	 *   </code>
	 * </pre>
	 *
	 * By default getCustomer() returns a response that includes all fields. The
	 * client can also reduce the size of the response by providing a so-called
	 * <a href=
	 * "https://www.microservice-api-patterns.org/patterns/quality/dataTransferParsimony/WishList">Wish
	 * List</a> with the query parameter {@code fields}:
	 *
	 * <pre>
	 *   <code>
	 * GET http://localhost:8110/customers/ce4btlyluu,rgpp0wkpec?fields=firstname,lastname
	 *
	 * {
	 *   "customers" : [
	 *     {
	 *       "firstname" : "Robbie",
	 *       "lastname" : "Davenhall",
	 *       "_links" : { ... }
	 *     },
	 *     {
	 *       "firstname" : "Max",
	 *       "lastname" : "Mustermann",
	 *       "_links" : { ... }
	 *     }
	 *   ],
	 *   "_links" : {
	 *     "self" : {
	 *       "href" : "http://localhost:8110/customers/ce4btlyluu,rgpp0wkpec?fields="
	 *     }
	 *   }
	 * }
	 *   </code>
	 * </pre>
	 *
	 * @see <a href=
	 *      "https://www.microservice-api-patterns.org/patterns/quality/dataTransferParsimony/RequestBundle">https://www.microservice-api-patterns.org/patterns/quality/dataTransferParsimony/RequestBundle</a>
	 * @see <a href=
	 *      "https://www.microservice-api-patterns.org/patterns/quality/dataTransferParsimony/WishList">https://www.microservice-api-patterns.org/patterns/quality/dataTransferParsimony/WishList</a>
	 */
	@Operation(summary = "Get a specific set of customers.")
	@GetMapping(value = "/{ids}") // MAP operation responsibility: Retrieval Operation
	public ResponseEntity<CustomersResponseDto> getCustomer(
			@Parameter(description = "a comma-separated list of customer ids", required = true) @PathVariable String ids,
			@Parameter(description = "a comma-separated list of the fields that should be included in the response", required = false) @RequestParam(value = "fields", required = false, defaultValue = "") String fields,
			@RequestHeader(value = "rest-tester", required = false) String restTester) {
		
		if(restTester == null){
			logger.info("TRIGGERED EDGE");
		} else {
			logger.info("TRIGGERED NODE: " + restTester);
		} 
		List<CustomerAggregateRoot> customers = customerService.getCustomers(ids);
		List<CustomerResponseDto> customerResponseDtos = customers.stream()
				.map(customer -> createCustomerResponseDto(customer, fields)).collect(Collectors.toList());
		CustomersResponseDto customersResponseDto = new CustomersResponseDto(customerResponseDtos);
		Link selfLink = linkTo(methodOn(CustomerInformationHolder.class).getCustomer(ids, fields, "link")).withSelfRel();
		customersResponseDto.add(selfLink);
		return ResponseEntity.ok(customersResponseDto);
	}

	@Operation(summary = "Update the profile of the customer with the given customer id")
	@PutMapping(value = "/{customerId}") // MAP operation responsibility: State Transition Operation (Full Replace)
	public ResponseEntity<CustomerResponseDto> updateCustomer(
			@Parameter(description = "the customer's unique id", required = true) @PathVariable CustomerId customerId,
			@Parameter(description = "the customer's updated profile", required = true) @Valid @RequestBody CustomerProfileUpdateRequestDto requestDto) {
		final CustomerProfileEntity updatedCustomerProfile = requestDto.toDomainObject();

		Optional<CustomerAggregateRoot> optCustomer = customerService.updateCustomerProfile(customerId, updatedCustomerProfile);
		if(!optCustomer.isPresent()) {
			final String errorMessage = "Failed to find a customer with id '" + customerId.toString() + "'.";
			logger.info(errorMessage);
			throw new CustomerNotFoundException(errorMessage);
		}

		CustomerAggregateRoot customer = optCustomer.get();
		CustomerResponseDto response = new CustomerResponseDto(Collections.emptySet(), customer);
		return ResponseEntity.ok(response);
	}
	
	@Operation(summary = "Change a customer's address.")
	@PutMapping(value = "/{customerId}/address") // MAP operation responsibility: State Transition Operation (Partial Update)
	public ResponseEntity<AddressDto> changeAddress(
			@Parameter(description = "the customer's unique id", required = true) @PathVariable CustomerId customerId,
			@Parameter(description = "the customer's new address", required = true) @Valid @RequestBody AddressDto requestDto,
			@RequestHeader(value = "rest-tester", required = false) String restTester) {
		
		if(restTester == null){
			logger.info("TRIGGERED EDGE");
		} else {
			logger.info("TRIGGERED NODE: " + restTester);
		} 

		Address updatedAddress = requestDto.toDomainObject();
		Optional<CustomerAggregateRoot> optCustomer = customerService.updateAddress(customerId, updatedAddress);
		if (!optCustomer.isPresent()) {
			final String errorMessage = "Failed to find a customer with id '" + customerId.toString() + "'.";
			logger.info(errorMessage);
			throw new CustomerNotFoundException(errorMessage);
		}

		AddressDto responseDto = AddressDto.fromDomainObject(updatedAddress);
		return ResponseEntity.ok(responseDto);
	}

	@Operation(summary = "Create a new customer.")
	@PostMapping // MAP operation responsibility: State Creation Operation
	public ResponseEntity<CustomerResponseDto> createCustomer(
			@Parameter(description = "the customer's profile information", required = true) @Valid @RequestBody CustomerProfileUpdateRequestDto requestDto,
			@RequestHeader(value = "rest-tester", required = false) String restTester) {
		
		if(restTester == null){
			logger.info("TRIGGERED EDGE");
		} else {
			logger.info("TRIGGERED NODE: " + restTester);
		}
		CustomerProfileEntity customerProfile = requestDto.toDomainObject();
		CustomerAggregateRoot customer = customerService.createCustomer(customerProfile);
		return ResponseEntity.ok(createCustomerResponseDto(customer, ""));
	}
}
