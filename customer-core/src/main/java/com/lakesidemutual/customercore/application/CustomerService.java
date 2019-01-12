package com.lakesidemutual.customercore.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.lakesidemutual.customercore.domain.customer.Address;
import com.lakesidemutual.customercore.domain.customer.CustomerAggregateRoot;
import com.lakesidemutual.customercore.domain.customer.CustomerFactory;
import com.lakesidemutual.customercore.domain.customer.CustomerId;
import com.lakesidemutual.customercore.domain.customer.CustomerProfileEntity;
import com.lakesidemutual.customercore.infrastructure.CustomerRepository;
import org.microserviceapipatterns.domaindrivendesign.ApplicationService;

/**
 * The CustomerService class is an application service that is
 * responsible for creating, updating and retrieving customer entities.
 * */
@Component
public class CustomerService implements ApplicationService {
	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private CustomerFactory customerFactory;

	public CustomerAggregateRoot updateAddress(CustomerId customerId, Address updatedAddress) {
		Optional<CustomerAggregateRoot> optCustomer = customerRepository.findById(customerId);
		if (!optCustomer.isPresent()) {
			return null;
		}

		CustomerAggregateRoot customer = optCustomer.get();
		customer.moveToAddress(updatedAddress);
		customerRepository.save(customer);
		return customer;
	}

	public CustomerAggregateRoot updateCustomerProfile(CustomerId customerId, CustomerProfileEntity updatedCustomerProfile) {
		Optional<CustomerAggregateRoot> optCustomer = customerRepository.findById(customerId);
		if (!optCustomer.isPresent()) {
			return null;
		}

		CustomerAggregateRoot customer = optCustomer.get();
		customer.updateCustomerProfile(updatedCustomerProfile);
		customerRepository.save(customer);
		return customer;
	}

	public CustomerAggregateRoot createCustomer(CustomerProfileEntity customerProfile) {
		CustomerAggregateRoot customer = customerFactory.create(customerProfile);
		customerRepository.save(customer);
		return customer;
	}

	public List<CustomerAggregateRoot> getCustomers(String ids) {
		List<CustomerId> customerIds = Arrays.asList(ids.split(",")).stream().map(id -> new CustomerId(id.trim())).collect(Collectors.toList());

		List<CustomerAggregateRoot> customers = new ArrayList<>();
		for (CustomerId customerId : customerIds) {
			Optional<CustomerAggregateRoot> customer = customerRepository.findById(customerId);
			if (customer.isPresent()) {
				customers.add(customer.get());
			}
		}
		return customers;
	}

	public Page<CustomerAggregateRoot> getCustomers(String filter, int limit, int offset) {
		final List<CustomerAggregateRoot> filteredCustomers = customerRepository
				.findAll(new Sort(Sort.Direction.ASC, "customerProfile.firstname", "customerProfile.lastname")).stream()
				.filter(customer -> matchesFilter(customer, filter))
				.collect(Collectors.toList());
		final int size = filteredCustomers.size();
		final List<CustomerAggregateRoot> customers = filteredCustomers.stream().skip(offset).limit(limit).collect(Collectors.toList());
		return new Page<>(customers, offset, limit, size);
	}

	private boolean matchesFilter(CustomerAggregateRoot customer, String filter) {
		final String[] searchTerms = filter.toLowerCase().split(" ");
		final String firstname = customer.getCustomerProfile().getFirstname().toLowerCase();
		final String lastname = customer.getCustomerProfile().getLastname().toLowerCase();
		for (String searchTerm : searchTerms) {
			if (!searchTerm.isEmpty() && !firstname.contains(searchTerm) && !lastname.contains(searchTerm)) {
				return false;
			}
		}
		return true;
	}
}
