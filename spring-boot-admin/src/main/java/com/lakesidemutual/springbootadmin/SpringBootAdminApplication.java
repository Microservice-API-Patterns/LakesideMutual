package com.lakesidemutual.springbootadmin;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// The Spring Boot Admin application monitors the other sample applications and provides various insights. 
// See the project's GitHub page: https://github.com/codecentric/spring-boot-admin

@SpringBootApplication
@EnableAdminServer
public class SpringBootAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootAdminApplication.class, args);
    }
}
