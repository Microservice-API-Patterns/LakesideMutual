package com.lakesidemutual.customercore.interfaces;

// import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
// import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

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
import com.lakesidemutual.customercore.interfaces.dtos.customer.PaginatedCustomerResponseDto;
import com.lm.ccore.Customer;
import com.lm.ccore.GetCustomerRequest;
import com.lm.ccore.GetCustomerResponse;
import com.lm.ccore.Profile;


/**
 * Based on: https://spring.io/guides/gs/producing-web-service/ and https://spring.io/guides/gs/consuming-web-service/
 * TODO note that when testing with Postman or curl, Content Type header must be set to "text/xml" (maps to SOAP 1.1)!
 *
 * This WS controller gives clients access to the customer data. It is an example of the
 * <i>Information Holder Resource</i> pattern. This particular one is a special type of information holder called <i>Master Data Holder</i>.
 *
 * @see <a href="https://www.microservice-api-patterns.org/patterns/responsibility/endpointRoles/InformationHolderResource">Information Holder Resource</a>
 * @see <a href="https://www.microservice-api-patterns.org/patterns/responsibility/informationHolderEndpointTypes/MasterDataHolder">Master Data Holder</a>
 */

@Endpoint
public class CustomerInformationHolderSOAPAdapter {
	private static final String NAMESPACE_URI = "http://lm.com/ccore";

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

		/*
		Link selfLink = linkTo(
				methodOn(CustomerInformationHolderWebServiceAdapter.class).getCustomer(customer.getId().toString(), fields))
				.withSelfRel();

		Link updateAddressLink = linkTo(methodOn(CustomerInformationHolderWebServiceAdapter.class).changeAddress(customer.getId(), null))
				.withRel("address.change");
		customerResponseDto.add(selfLink);
		customerResponseDto.add(updateAddressLink);
		 */
		return customerResponseDto;
	}

	private PaginatedCustomerResponseDto createPaginatedCustomerResponseDto(String filter, Integer limit,
			Integer offset, int size, String fields, List<CustomerResponseDto> customerDtos) {

		PaginatedCustomerResponseDto paginatedCustomerResponseDto = new PaginatedCustomerResponseDto(filter, limit,
				offset, size, customerDtos);

		/*
		paginatedCustomerResponseDto
		.add(linkTo(methodOn(CustomerInformationHolderWebServiceAdapter.class).getCustomers(filter, limit, offset, fields))
				.withSelfRel());

		if (offset > 0) {
			paginatedCustomerResponseDto.add(linkTo(methodOn(CustomerInformationHolderWebServiceAdapter.class).getCustomers(filter,
					limit, Math.max(0, offset - limit), fields)).withRel("prev"));
		}

		if (offset < size - limit) {
			paginatedCustomerResponseDto.add(linkTo(
					methodOn(CustomerInformationHolderWebServiceAdapter.class).getCustomers(filter, limit, offset + limit, fields))
					.withRel("next"));
		}
		 */
		return paginatedCustomerResponseDto;
	}
	
	// TODO not exposed via SOAP/HTTP yet
	// MAP operation responsibility: Retrieval Operation
	public PaginatedCustomerResponseDto getCustomers(
			String filter,
			Integer limit,
			Integer offset,
			String fields) {

		final Page<CustomerAggregateRoot> customerPage = customerService.getCustomers(filter, limit, offset);
		List<CustomerResponseDto> customerDtos = customerPage.getElements().stream().map(c -> createCustomerResponseDto(c, fields)).collect(Collectors.toList());

		PaginatedCustomerResponseDto paginatedCustomerResponseDto = createPaginatedCustomerResponseDto(
				filter,
				customerPage.getLimit(),
				customerPage.getOffset(),
				customerPage.getSize(),
				fields,
				customerDtos);
		return paginatedCustomerResponseDto;
	}


	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "getCustomerRequest")
	@ResponsePayload 
	// MAP operation responsibility: Retrieval Operation
	public GetCustomerResponse getCustomer(
			@RequestPayload GetCustomerRequest request) {

		// TODO implement pagination and wishlist, support "old" method signature (?)
		/*
		public CustomersResponseDto getCustomer(
				@RequestPayload String ids , String fields) {
		 */
		GetCustomerResponse response = new GetCustomerResponse();

		// note that CAR works with n ids and I am passing a single one (?)
		List<CustomerAggregateRoot> customers = customerService.getCustomers(request.getId());
		CustomerAggregateRoot car = customers.get(0);
		Customer customerDTO = new Customer();
		customerDTO.setSegment(Profile.VIP); // TODO not yet in domain model
		customerDTO.setName(car.getCustomerProfile().getFirstname() + " " + car.getCustomerProfile().getLastname()); // TODO demo EIP IF/MT here?
		customerDTO.setAddress(car.getCustomerProfile().getCurrentAddress().toString());
		XMLGregorianCalendar bday = null; // TODO
		customerDTO.setBirthday(bday);
		response.setCustomer(customerDTO);

		return response;

		/*
		List<CustomerAggregateRoot> customers = customerService.getCustomers(ids);
		List<CustomerResponseDto> customerResponseDtos = customers.stream()
				.map(customer -> createCustomerResponseDto(customer, fields)).collect(Collectors.toList());

		CustomersResponseDto customersResponseDto = new CustomersResponseDto(customerResponseDtos);

		Link selfLink = linkTo(methodOn(CustomerInformationHolderWebServiceAdapter.class).getCustomer(ids, fields)).withSelfRel();
		customersResponseDto.add(selfLink);


		// return customersResponseDto;

		 */
	}

	// TODO not exposed via SOAP/HTTP yet
	// MAP operation responsibility: State Transition Operation (Full Replace)
	public CustomerResponseDto updateCustomer(
			CustomerId customerId,
			@Valid CustomerProfileUpdateRequestDto requestDto) {
		final CustomerProfileEntity updatedCustomerProfile = requestDto.toDomainObject();
		Optional<CustomerAggregateRoot> optCustomer = customerService.updateCustomerProfile(customerId, updatedCustomerProfile);
		if(!optCustomer.isPresent()) {
			final String errorMessage = "Failed to find a customer with id '" + customerId.toString() + "'.";
			logger.info(errorMessage);
			throw new CustomerNotFoundException(errorMessage);
		}

		CustomerAggregateRoot customer = optCustomer.get();
		CustomerResponseDto response = new CustomerResponseDto(Collections.emptySet(), customer);
		return response;
	}
	
	// TODO not exposed via SOAP/HTTP yet
	// MAP operation responsibility: State Transition Operation (Partial Update)
	public AddressDto changeAddress(
			CustomerId customerId,
			@Valid AddressDto requestDto) {

		Address updatedAddress = requestDto.toDomainObject();
		Optional<CustomerAggregateRoot> optCustomer = customerService.updateAddress(customerId, updatedAddress);
		if (!optCustomer.isPresent()) {
			final String errorMessage = "Failed to find a customer with id '" + customerId.toString() + "'.";
			logger.info(errorMessage);
			throw new CustomerNotFoundException(errorMessage);
		}

		return AddressDto.fromDomainObject(updatedAddress);
	}



	// TODO not exposed via SOAP/HTTP yet
	// MAP operation responsibility: State Creation Operation
	public CustomerResponseDto createCustomer(
			@Valid CustomerProfileUpdateRequestDto requestDto) {

		CustomerProfileEntity customerProfile = requestDto.toDomainObject();
		CustomerAggregateRoot customer = customerService.createCustomer(customerProfile);
		return createCustomerResponseDto(customer, "");
	}
}
