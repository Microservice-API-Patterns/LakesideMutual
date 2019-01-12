package com.lakesidemutual.customermanagement.infrastructure;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

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
import org.springframework.web.util.UriComponentsBuilder;

import com.lakesidemutual.customermanagement.domain.customer.CustomerId;
import com.lakesidemutual.customermanagement.interfaces.dtos.CustomerCoreNotAvailableException;
import com.lakesidemutual.customermanagement.interfaces.dtos.CustomerDto;
import com.lakesidemutual.customermanagement.interfaces.dtos.CustomerProfileDto;
import com.lakesidemutual.customermanagement.interfaces.dtos.CustomersDto;
import com.lakesidemutual.customermanagement.interfaces.dtos.PaginatedCustomerResponseDto;

/**
 * CustomerCoreService is a remote proxy that interacts with the Customer Core in order to give
 * the Customer Management Backend's own clients access to the shared customer data.
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
			CustomersDto customersDto = restTemplate.getForObject(url, CustomersDto.class);
			return customersDto.getCustomers().isEmpty() ? null : customersDto.getCustomers().get(0);
		} catch(RestClientException e) {
			final String errorMessage = "Failed to connect to Customer Core.";
			logger.info(errorMessage, e);
			throw new CustomerCoreNotAvailableException(errorMessage);
		}
	}

	public PaginatedCustomerResponseDto getCustomers(String filter, int limit, int offset) {
		try {
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(customerCoreBaseURL + "/customers")
					.queryParam("filter", filter)
					.queryParam("limit", limit)
					.queryParam("offset", offset);
			return restTemplate.getForObject(builder.toUriString(), PaginatedCustomerResponseDto.class);
		} catch(RestClientException e) {
			final String errorMessage = "Failed to connect to Customer Core.";
			logger.info(errorMessage, e);
			throw new CustomerCoreNotAvailableException(errorMessage);
		}
	}

	public ResponseEntity<CustomerDto> updateCustomer(CustomerId customerId, CustomerProfileDto customerProfile) {
		try {
			final String url = customerCoreBaseURL + "/customers/" + customerId.getId();
			final HttpEntity<CustomerProfileDto> requestEntity = new HttpEntity<>(customerProfile);
			return restTemplate.exchange(url, HttpMethod.PUT, requestEntity, CustomerDto.class);
		} catch(RestClientException e) {
			final String errorMessage = "Failed to connect to Customer Core.";
			logger.info(errorMessage, e);
			throw new CustomerCoreNotAvailableException(errorMessage);
		}
	}
}
