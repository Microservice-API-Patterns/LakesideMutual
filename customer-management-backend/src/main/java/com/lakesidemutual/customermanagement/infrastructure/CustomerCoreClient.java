package com.lakesidemutual.customermanagement.infrastructure;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.lakesidemutual.customermanagement.domain.customer.CustomerId;
import com.lakesidemutual.customermanagement.interfaces.dtos.CustomerDto;
import com.lakesidemutual.customermanagement.interfaces.dtos.CustomerProfileDto;
import com.lakesidemutual.customermanagement.interfaces.dtos.CustomersDto;
import com.lakesidemutual.customermanagement.interfaces.dtos.PaginatedCustomerResponseDto;

/**
 * CustomerCoreClient is a FeignClient interface that is used to declare a web service client for the customer-core service.
 * */
@FeignClient(name="customercore", url="${customercore.baseURL}", configuration=CustomerCoreClientConfiguration.class)
public interface CustomerCoreClient {
	@GetMapping(value = "/customers")
	PaginatedCustomerResponseDto getCustomers(
			@RequestParam(value = "filter") String filter,
			@RequestParam(value = "limit") Integer limit,
			@RequestParam(value = "offset") Integer offset);

	@GetMapping(value = "/customers/{ids}")
	CustomersDto getCustomer(
			@PathVariable String ids);

	@PutMapping(value = "/customers/{customerId}")
	ResponseEntity<CustomerDto> updateCustomer(
			@PathVariable CustomerId customerId,
			@RequestBody CustomerProfileDto requestDto);
}