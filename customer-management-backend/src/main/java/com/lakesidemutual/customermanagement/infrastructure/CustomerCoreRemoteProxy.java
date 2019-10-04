package com.lakesidemutual.customermanagement.infrastructure;

import org.microserviceapipatterns.domaindrivendesign.InfrastructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.lakesidemutual.customermanagement.domain.customer.CustomerId;
import com.lakesidemutual.customermanagement.interfaces.dtos.CustomerCoreNotAvailableException;
import com.lakesidemutual.customermanagement.interfaces.dtos.CustomerDto;
import com.lakesidemutual.customermanagement.interfaces.dtos.CustomerProfileDto;
import com.lakesidemutual.customermanagement.interfaces.dtos.CustomersDto;
import com.lakesidemutual.customermanagement.interfaces.dtos.PaginatedCustomerResponseDto;

import feign.FeignException;

/**
 * CustomerCoreRemoteProxy is a remote proxy that interacts with the Customer
 * Core in order to give the Customer Management Backend's own clients access to
 * the shared customer data.
 */
@Component
public class CustomerCoreRemoteProxy implements InfrastructureService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CustomerCoreClient customerCoreClient;

	public CustomerDto getCustomer(CustomerId customerId) {
		try {
			CustomersDto customersDto = customerCoreClient.getCustomer(customerId.getId());
			return customersDto.getCustomers().isEmpty() ? null : customersDto.getCustomers().get(0);
		} catch(FeignException e) {
			final String errorMessage = "Failed to connect to Customer Core.";
			logger.info(errorMessage, e);
			throw new CustomerCoreNotAvailableException(errorMessage);
		}
	}

	public PaginatedCustomerResponseDto getCustomers(String filter, int limit, int offset) {
		try {
			return customerCoreClient.getCustomers(filter, limit, offset);
		} catch(FeignException e) {
			final String errorMessage = "Failed to connect to Customer Core.";
			logger.info(errorMessage, e);
			throw new CustomerCoreNotAvailableException(errorMessage);
		}
	}

	public ResponseEntity<CustomerDto> updateCustomer(CustomerId customerId, CustomerProfileDto customerProfile) {
		try {
			return customerCoreClient.updateCustomer(customerId, customerProfile);
		} catch(FeignException e) {
			final String errorMessage = "Failed to connect to Customer Core.";
			logger.info(errorMessage, e);
			throw new CustomerCoreNotAvailableException(errorMessage);
		}
	}
}
