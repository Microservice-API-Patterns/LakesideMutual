package com.lakesidemutual.customerselfservice.infrastructure;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.microserviceapipatterns.domaindrivendesign.InfrastructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.lakesidemutual.customerselfservice.domain.customer.CustomerId;
import com.lakesidemutual.customerselfservice.interfaces.dtos.city.CitiesResponseDto;
import com.lakesidemutual.customerselfservice.interfaces.dtos.customer.AddressDto;
import com.lakesidemutual.customerselfservice.interfaces.dtos.customer.CustomerCoreNotAvailableException;
import com.lakesidemutual.customerselfservice.interfaces.dtos.customer.CustomerDto;
import com.lakesidemutual.customerselfservice.interfaces.dtos.customer.CustomerProfileUpdateRequestDto;
import com.lakesidemutual.customerselfservice.interfaces.dtos.customer.CustomersDto;

/**
 * CustomerCoreService is a remote proxy that interacts with the Customer Core in order to give
 * the Customer Self-Service Backend's own clients access to the shared customer data.
 * */
@Component
public class CustomerCoreService implements InfrastructureService {
	@Value("${customercore.baseURL}")
	private String customerCoreBaseURL;

	@Value("${apikey.header}")
	private String apiKeyHeader;

	@Value("${apikey.value}")
	private String apiKeyValue;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private RestTemplate restTemplate;

	class HeaderRequestInterceptor implements ClientHttpRequestInterceptor {
		@Override
		public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
			final HttpHeaders httpHeaders = request.getHeaders();
			httpHeaders.set(apiKeyHeader, "Bearer " + apiKeyValue);
			httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			return execution.execute(request, body);
		}
	}

	public CustomerCoreService() {
		restTemplate = new RestTemplate();
		restTemplate.setInterceptors(Arrays.asList(new HeaderRequestInterceptor()));
	}

	public CustomerDto getCustomer(CustomerId customerId) {
		try {
			final String url = customerCoreBaseURL + "/customers/" + customerId.getId();
			List<CustomerDto> customers = restTemplate.getForObject(url, CustomersDto.class).getCustomers();
			return customers.isEmpty() ? null : customers.get(0);
		} catch(RestClientException e) {
			final String errorMessage = "Failed to connect to Customer Core.";
			logger.info(errorMessage, e);
			throw new CustomerCoreNotAvailableException(errorMessage);
		}
	}

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
}
