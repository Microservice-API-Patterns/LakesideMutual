package com.lakesidemutual.customerselfservice.infrastructure;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lakesidemutual.customerselfservice.domain.customer.CustomerId;
import com.lakesidemutual.customerselfservice.domain.insurancequoterequest.InsuranceQuoteRequestAggregateRoot;
import org.microserviceapipatterns.domaindrivendesign.Repository;

/**
 * The InsuranceQuoteRequestRepository can be used to read and write InsuranceQuoteRequestAggregateRoot objects from and to the backing database. Spring automatically
 * searches for interfaces that extend the JpaRepository interface and creates a corresponding Spring bean for each of them. For more information
 * on repositories visit the <a href="https://docs.spring.io/spring-data/jpa/docs/current/reference/html/">Spring Data JPA - Reference Documentation</a>.
 * */
public interface InsuranceQuoteRequestRepository extends JpaRepository<InsuranceQuoteRequestAggregateRoot, Long>, Repository {
	List<InsuranceQuoteRequestAggregateRoot> findByCustomerInfo_CustomerIdOrderByDateDesc(CustomerId customerId);
	List<InsuranceQuoteRequestAggregateRoot> findAllByOrderByDateDesc();
}