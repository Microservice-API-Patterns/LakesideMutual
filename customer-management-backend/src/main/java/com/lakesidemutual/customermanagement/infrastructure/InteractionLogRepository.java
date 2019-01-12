package com.lakesidemutual.customermanagement.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lakesidemutual.customermanagement.domain.interactionlog.InteractionLogAggregateRoot;
import org.microserviceapipatterns.domaindrivendesign.Repository;

/**
 * The InteractionLogRepository can be used to read and write InteractionLogAggregateRoot objects from and to the backing database. Spring automatically
 * searches for interfaces that extend the JpaRepository interface and creates a corresponding Spring bean for each of them. For more information
 * on repositories visit the <a href="https://docs.spring.io/spring-data/jpa/docs/current/reference/html/">Spring Data JPA - Reference Documentation</a>.
 * */
public interface InteractionLogRepository extends JpaRepository<InteractionLogAggregateRoot, String>, Repository {

}
