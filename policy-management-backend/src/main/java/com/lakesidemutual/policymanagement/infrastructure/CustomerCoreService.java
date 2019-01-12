package com.lakesidemutual.policymanagement.infrastructure;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.microserviceapipatterns.domaindrivendesign.InfrastructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.lakesidemutual.policymanagement.domain.customer.CustomerId;
import com.lakesidemutual.policymanagement.interfaces.dtos.CustomerCoreNotAvailableException;
import com.lakesidemutual.policymanagement.interfaces.dtos.customer.CustomerDto;
import com.lakesidemutual.policymanagement.interfaces.dtos.customer.CustomersDto;
import com.lakesidemutual.policymanagement.interfaces.dtos.customer.PaginatedCustomerResponseDto;

/**
 * CustomerCoreService is a remote proxy that interacts with the Customer Core in order to give
 * the Policy Management Backend's own clients access to the shared customer data.
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
		List<CustomerDto> customers = getCustomersById(customerId);
		return customers.isEmpty() ? null : customers.get(0);
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

	public List<CustomerDto> getCustomersById(CustomerId... customerIds) {
		try {
			List<String> customerIdStrings = Arrays.asList(customerIds).stream().map(id -> id.getId()).collect(Collectors.toList());
			String ids = String.join(",", customerIdStrings);
			return restTemplate.getForObject(customerCoreBaseURL + "/customers/" + ids, CustomersDto.class).getCustomers();
		} catch(RestClientException e) {
			final String errorMessage = "Failed to connect to Customer Core.";
			logger.info(errorMessage, e);
			throw new CustomerCoreNotAvailableException(errorMessage);
		}
	}
}
