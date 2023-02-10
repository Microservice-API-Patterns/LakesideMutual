package com.lakesidemutual.customercore.application;

import com.lakesidemutual.customercore.domain.customer.*;
import com.lakesidemutual.customercore.infrastructure.CustomerRepository;
import org.hibernate.jpa.QueryHints;
import org.microserviceapipatterns.domaindrivendesign.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * The CustomerService class is an application service that is
 * responsible for creating, updating and retrieving customer entities.
 */
@Component
public class CustomerService implements ApplicationService {
	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private CustomerFactory customerFactory;

	public Optional<CustomerAggregateRoot> updateAddress(CustomerId customerId, Address updatedAddress) {
		Optional<CustomerAggregateRoot> optCustomer = customerRepository.findById(customerId);
		if (!optCustomer.isPresent()) {
			return optCustomer;
		}

		CustomerAggregateRoot customer = optCustomer.get();
		customer.moveToAddress(updatedAddress);
		customerRepository.save(customer);
		return optCustomer;
	}

	public Optional<CustomerAggregateRoot> updateCustomerProfile(CustomerId customerId, CustomerProfileEntity updatedCustomerProfile) {
		Optional<CustomerAggregateRoot> optCustomer = customerRepository.findById(customerId);
		if (!optCustomer.isPresent()) {
			return optCustomer;
		}

		CustomerAggregateRoot customer = optCustomer.get();
		customer.updateCustomerProfile(updatedCustomerProfile);
		customerRepository.save(customer);
		return optCustomer;
	}

	public CustomerAggregateRoot createCustomer(CustomerProfileEntity customerProfile) {
		CustomerAggregateRoot customer = customerFactory.create(customerProfile);
		customerRepository.save(customer);
		return customer;
	}

	public List<CustomerAggregateRoot> getCustomers(String ids) {
		List<CustomerId> customerIds = Arrays.stream(ids.split(",")).map(id -> new CustomerId(id.trim())).collect(Collectors.toList());

		List<CustomerAggregateRoot> customers = new ArrayList<>();
		for (CustomerId customerId : customerIds) {
			Optional<CustomerAggregateRoot> customer = customerRepository.findById(customerId);
			customer.ifPresent(customers::add);
		}
		return customers;
	}

	public Page<CustomerAggregateRoot> getCustomers(String filter, int limit, int offset) {

		// See https://vladmihalcea.com/fix-hibernate-hhh000104-entity-fetch-pagination-warning-message/
		// for details on the following implementation:

		String filterParameter = "%" + filter + "%";

		long totalSize = entityManager.createQuery(
						"select count(1) from CustomerAggregateRoot c " +
								"left join fetch c.customerProfile " +
								"where c.customerProfile.firstname like :filter or c.customerProfile.lastname like :filter", Long.class)
				.setParameter("filter", filterParameter)
				.getSingleResult();

		List<CustomerId> customerIds = entityManager.createQuery(
						"select c.id from CustomerAggregateRoot c " +
								"left join fetch c.customerProfile " +
								"where c.customerProfile.firstname like :filter or c.customerProfile.lastname like :filter " +
								"order by c.customerProfile.firstname, c.customerProfile.lastname", CustomerId.class)
				.setParameter("filter", filterParameter)
				.setFirstResult(offset)
				.setMaxResults(limit)
				.getResultList();

		List<CustomerAggregateRoot> customerAggregateRoots = entityManager.createQuery(
						"select c from CustomerAggregateRoot c " +
								"left join fetch c.customerProfile " +
								"left join fetch c.customerProfile.moveHistory " +
								"where c.id in (:customerIds) " +
								"order by c.customerProfile.firstname, c.customerProfile.lastname", CustomerAggregateRoot.class)
				.setParameter("customerIds", customerIds)
				.setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
				.getResultList();

		return new Page<>(customerAggregateRoots, offset, limit, (int) totalSize);
	}
}
