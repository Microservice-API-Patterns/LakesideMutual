package com.lakesidemutual.customercore.interfaces;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

	/**
	 * Attributes marked with @Autowired are automatically created and injected by
	 * Spring.
	 */
	@Autowired
	private CustomerService customerService;

	@ApiOperation(value = "Change a customer's address.")
	@PutMapping(value = "/{customerId}/address")
	public ResponseEntity<AddressDto> changeAddress(
			@ApiParam(value = "the customer's unique id", required = true) @PathVariable CustomerId customerId,
			@ApiParam(value = "the customer's new address", required = true) @Valid @RequestBody AddressDto requestDto) {

		Address updatedAddress = new Address(requestDto.getStreetAddress(), requestDto.getPostalCode(), requestDto.getCity());
		CustomerAggregateRoot customer = customerService.updateAddress(customerId, updatedAddress);
		if (customer == null) {
			final String errorMessage = "Failed to find a customer with id '" + customerId.toString() + "'.";
			logger.info(errorMessage);
			throw new CustomerNotFoundException(errorMessage);
		}

		AddressDto responseDto = new AddressDto(updatedAddress.getStreetAddress(), updatedAddress.getPostalCode(), updatedAddress.getCity());
		return ResponseEntity.ok(responseDto);
	}

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
				methodOn(CustomerInformationHolder.class).getCustomer(customer.getId().toString(), fields))
				.withSelfRel();

		Link updateAddressLink = linkTo(methodOn(CustomerInformationHolder.class).changeAddress(customer.getId(), null))
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
		.add(linkTo(methodOn(CustomerInformationHolder.class).getCustomers(filter, limit, offset, fields))
				.withSelfRel());

		if (offset > 0) {
			paginatedCustomerResponseDto.add(linkTo(methodOn(CustomerInformationHolder.class).getCustomers(filter,
					limit, Math.max(0, offset - limit), fields)).withRel("prev"));
		}

		if (offset < size - limit) {
			paginatedCustomerResponseDto.add(linkTo(
					methodOn(CustomerInformationHolder.class).getCustomers(filter, limit, offset + limit, fields))
					.withRel("next"));
		}

		return paginatedCustomerResponseDto;
	}

	/**
	 * Returns the customers for the given customer ids. <br>
	 * <br>
	 * The client can provide a comma-separated list of customer ids to fetch a
	 * particular set of customers. This is a variant of the <a href=
	 * "http://www.microservice-api-patterns.org/patterns/quality/dataTransferParsimony/WADE-RequestBundle.html">Request
	 * Bundle</a> pattern:
	 *
	 * <pre>
	 *   <code>
	 * GET http://localhost:8080/customers/ce4btlyluu,rgpp0wkpec
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
	 *       "href" : "http://localhost:8080/customers/ce4btlyluu,rgpp0wkpec?fields="
	 *     }
	 *   }
	 * }
	 *   </code>
	 * </pre>
	 *
	 * By default getCustomer() returns a response that includes all fields. The
	 * client can also reduce the size of the response by providing a so-called
	 * <a href=
	 * "http://www.microservice-api-patterns.org/patterns/quality/WADE-WishList.html">Wish
	 * List</a> with the query parameter {@code fields}:
	 *
	 * <pre>
	 *   <code>
	 * GET http://localhost:8080/customers/ce4btlyluu,rgpp0wkpec?fields=firstname,lastname
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
	 *       "href" : "http://localhost:8080/customers/ce4btlyluu,rgpp0wkpec?fields="
	 *     }
	 *   }
	 * }
	 *   </code>
	 * </pre>
	 *
	 * @see <a href=
	 *      "http://www.microservice-api-patterns.org/patterns/quality/dataTransferParsimony/WADE-RequestBundle.html">www.microservice-api-patterns.org/patterns/quality/dataTransferParsimony/WADE-RequestBundle.html</a>
	 * @see <a href=
	 *      "http://www.microservice-api-patterns.org/patterns/quality/WADE-WishList.html">www.microservice-api-patterns.org/patterns/quality/WADE-WishList.html</a>
	 */
	@ApiOperation(value = "Get a specific set of customers.")
	@GetMapping(value = "/{ids}")
	public ResponseEntity<CustomersResponseDto> getCustomer(
			@ApiParam(value = "a comma-separated list of customer ids", required = true) @PathVariable String ids,
			@ApiParam(value = "a comma-separated list of the fields that should be included in the response", required = false) @RequestParam(value = "fields", required = false, defaultValue = "") String fields) {

		List<CustomerAggregateRoot> customers = customerService.getCustomers(ids);
		List<CustomerResponseDto> customerResponseDtos = customers.stream()
				.map(customer -> createCustomerResponseDto(customer, fields)).collect(Collectors.toList());
		CustomersResponseDto customersResponseDto = new CustomersResponseDto(customerResponseDtos);
		Link selfLink = linkTo(methodOn(CustomerInformationHolder.class).getCustomer(ids, fields)).withSelfRel();
		customersResponseDto.add(selfLink);
		return ResponseEntity.ok(customersResponseDto);
	}

	@ApiOperation(value = "Update the profile of the customer with the given customer id")
	@PutMapping(value = "/{customerId}")
	public ResponseEntity<CustomerResponseDto> updateCustomer(
			@ApiParam(value = "the customer's unique id", required = true) @PathVariable CustomerId customerId,
			@ApiParam(value = "the customer's updated profile", required = true) @Valid @RequestBody CustomerProfileUpdateRequestDto requestDto) {
		final Address address = new Address(requestDto.getStreetAddress(), requestDto.getPostalCode(), requestDto.getCity());
		final CustomerProfileEntity updatedCustomerProfile = new CustomerProfileEntity(
				requestDto.getFirstname(),
				requestDto.getLastname(),
				requestDto.getBirthday(),
				address,
				requestDto.getEmail(),
				requestDto.getPhoneNumber()
				);

		CustomerAggregateRoot customer = customerService.updateCustomerProfile(customerId, updatedCustomerProfile);
		if(customer == null) {
			final String errorMessage = "Failed to find a customer with id '" + customerId.toString() + "'.";
			logger.info(errorMessage);
			throw new CustomerNotFoundException(errorMessage);
		}

		CustomerResponseDto response = new CustomerResponseDto(Collections.emptySet(), customer);
		return ResponseEntity.ok(response);
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
	 * GET http://localhost:8080/customers?limit=2&offset=2
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
	 *       "href" : "http://localhost:8080/customers?limit=2&offset=2&fields="
	 *     },
	 *     "prev" : {
	 *       "href" : "http://localhost:8080/customers?limit=2&offset=0&fields="
	 *     },
	 *     "next" : {
	 *       "href" : "http://localhost:8080/customers?limit=2&offset=4&fields="
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
	 *      "http://www.microservice-api-patterns.org/patterns/structure/compositeRepresentations/WADE-Pagination.html">www.microservice-api-patterns.org/patterns/structure/compositeRepresentations/WADE-Pagination.html</a>
	 */
	@ApiOperation(value = "Get all customers in pages of 10 entries per page.")
	@GetMapping
	public ResponseEntity<PaginatedCustomerResponseDto> getCustomers(
			@ApiParam(value = "search terms to filter the customers by name", required = false) @RequestParam(value = "filter", required = false, defaultValue = "") String filter,
			@ApiParam(value = "the maximum number of customers per page", required = false) @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
			@ApiParam(value = "the offset of the page's first customer", required = false) @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
			@ApiParam(value = "a comma-separated list of the fields that should be included in the response", required = false) @RequestParam(value = "fields", required = false, defaultValue = "") String fields) {

		final Page<CustomerAggregateRoot> customerPage = customerService.getCustomers(filter, limit, offset);
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

	@ApiOperation(value = "Create a new customer.")
	@PostMapping
	public ResponseEntity<CustomerResponseDto> createCustomer(
			@ApiParam(value = "the customer's profile information", required = true) @Valid @RequestBody CustomerProfileUpdateRequestDto reuestDto) {

		Address address = new Address(reuestDto.getStreetAddress(), reuestDto.getPostalCode(), reuestDto.getCity());
		CustomerProfileEntity customerProfile = new CustomerProfileEntity(
				reuestDto.getFirstname(), reuestDto.getLastname(), reuestDto.getBirthday(),
				address, reuestDto.getEmail(), reuestDto.getPhoneNumber());
		CustomerAggregateRoot customer = customerService.createCustomer(customerProfile);
		return ResponseEntity.ok(createCustomerResponseDto(customer, ""));
	}
}
