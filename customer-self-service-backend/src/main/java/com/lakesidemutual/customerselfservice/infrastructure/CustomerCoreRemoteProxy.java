package com.lakesidemutual.customerselfservice.infrastructure;

import java.util.List;

import org.microserviceapipatterns.domaindrivendesign.InfrastructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.lakesidemutual.customerselfservice.domain.customer.CustomerId;
import com.lakesidemutual.customerselfservice.interfaces.dtos.city.CitiesResponseDto;
import com.lakesidemutual.customerselfservice.interfaces.dtos.customer.AddressDto;
import com.lakesidemutual.customerselfservice.interfaces.dtos.customer.CustomerCoreNotAvailableException;
import com.lakesidemutual.customerselfservice.interfaces.dtos.customer.CustomerDto;
import com.lakesidemutual.customerselfservice.interfaces.dtos.customer.CustomerProfileDto;
import com.lakesidemutual.customerselfservice.interfaces.dtos.customer.CustomerProfileUpdateRequestDto;
import com.lakesidemutual.customerselfservice.interfaces.dtos.customer.CustomersDto;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

/**
 * CustomerCoreRemoteProxy is a remote proxy that interacts with the Customer Core in order to give
 * the Customer Self-Service Backend's own clients access to the shared customer data.
 * */
@Component
public class CustomerCoreRemoteProxy implements InfrastructureService, CustomerCoreServiceMBean {
	@Value("${customercore.baseURL}")
	private String customerCoreBaseURL;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private RestTemplate restTemplate;

	int successfulAttemptsCounter=0;
	int unsuccessfulAttemptsCounter=0;
	int fallBackMethodExecutionsCounter=0;

	// TODO test/demo that protected methods actually stops executing for defined time period,
	// otherwise the circuit breaker does not do much more than a regular exception handler
	@HystrixCommand(
			fallbackMethod = "getDummyCustomer" )
	public CustomerDto getCustomer(CustomerId customerId) {
		try {
			final String url = customerCoreBaseURL + "/customers/" + customerId.getId();
			logger.info("About to GET customer with id " + customerId);
			List<CustomerDto> customers = restTemplate.getForObject(url, CustomersDto.class).getCustomers();
			successfulAttemptsCounter++;
			return customers.isEmpty() ? null : customers.get(0);
		} catch(RestClientException e) {
			final String errorMessage = "Failed to connect to Customer Core.";
			logger.info(errorMessage, e);
			unsuccessfulAttemptsCounter++;
			throw new CustomerCoreNotAvailableException(errorMessage);
		}
	}

	public CustomerDto getDummyCustomer(CustomerId customerId) {
		logger.warn("Circuit Breaker active, failed to connect to Customer Core!");

		// TODO return dummy/default customer (from local DB)?
		fallBackMethodExecutionsCounter++;

		CustomerDto customerCopy = new CustomerDto();
		customerCopy.setCustomerId(customerId.toString());
		CustomerProfileDto emptyProfile = new CustomerProfileDto();
		emptyProfile.setFirstname("WARNING: No first name info available");
		emptyProfile.setLastname("WARNING: No last name info available");
		emptyProfile.setEmail("it-support@lm.com");
		emptyProfile.setPhoneNumber("WARNING: No phone number info available");
		AddressDto currentAddress = new AddressDto();
		currentAddress.setStreetAddress("n/a");
		currentAddress.setPostalCode("n/a");
		currentAddress.setCity("n/a");
		emptyProfile.setCurrentAddress(currentAddress );
		customerCopy.setCustomerProfile(emptyProfile);
		return customerCopy;
	}

	// TODO protect other methods too?

	public ResponseEntity<AddressDto> changeAddress(CustomerId customerId, AddressDto requestDto) {
		try {
			final String url = customerCoreBaseURL + "/customers/" + customerId.getId() + "/address";
			final HttpEntity<AddressDto> requestEntity = new HttpEntity<>(requestDto);
			return restTemplate.exchange(url, HttpMethod.PUT, requestEntity, AddressDto.class);
		} catch(RestClientException e) {
			final String errorMessage = "Failed to connect to Customer Core.";
			logger.info(errorMessage, e);
			throw new CustomerCoreNotAvailableException(errorMessage);
		}
	}

	public CustomerDto createCustomer(CustomerProfileUpdateRequestDto requestDto) {
		try {
			final String url = customerCoreBaseURL + "/customers";
			return restTemplate.postForObject(url, requestDto, CustomerDto.class);
		} catch(RestClientException e) {
			final String errorMessage = "Failed to connect to Customer Core.";
			logger.info(errorMessage, e);
			throw new CustomerCoreNotAvailableException(errorMessage);
		}
	}

	public ResponseEntity<CitiesResponseDto> getCitiesForPostalCode(String postalCode) {
		try {
			final String url = customerCoreBaseURL + "/cities/" + postalCode;
			return restTemplate.getForEntity(url, CitiesResponseDto.class);
		} catch(RestClientException e) {
			final String errorMessage = "Failed to connect to Customer Core.";
			logger.info(errorMessage, e);
			throw new CustomerCoreNotAvailableException(errorMessage);
		}
	}

	@Override
	public int getSuccessfullAttemptsCounter() {
		return successfulAttemptsCounter;
	}

	@Override
	public int getUnuccessfullAttemptsCounter() {
		return unsuccessfulAttemptsCounter;
	}

	@Override
	public int getFallbackMethodExecutionCounter() {
		return fallBackMethodExecutionsCounter;
	}


	@Override
	public void resetCounters() {
		successfulAttemptsCounter=0;
		unsuccessfulAttemptsCounter=0;
		fallBackMethodExecutionsCounter=0;
	}

}
