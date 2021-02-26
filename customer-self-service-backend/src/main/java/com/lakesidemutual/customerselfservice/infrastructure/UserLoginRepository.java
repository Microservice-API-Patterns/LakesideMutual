package com.lakesidemutual.customerselfservice.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lakesidemutual.customerselfservice.domain.identityaccess.UserLoginEntity;
import org.microserviceapipatterns.domaindrivendesign.Repository;

/**
 * The UserLoginRepository can be used to read and write UserLogin objects from and to the backing database. Spring automatically
 * searches for interfaces that extend the JpaRepository interface and creates a corresponding Spring bean for each of them. For more information
 * on repositories visit the <a href="https://docs.spring.io/spring-data/jpa/docs/current/reference/html/">Spring Data JPA - Reference Documentation</a>.
 * */
public interface UserLoginRepository extends JpaRepository<UserLoginEntity, Long>, Repository {

	public UserLoginEntity findByEmail(String email);

}
