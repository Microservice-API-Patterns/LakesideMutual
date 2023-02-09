# ![Lakeside Mutual Logo](../resources/logo-32x32.png) Lakeside Mutual: Customer Self-Service Backend

The Customer Self-Service backend provides an HTTP resource API for the Customer Self-Service frontend. Additionally, it connects
to an [ActiveMQ](http://activemq.apache.org/) broker provided by the Policy Management backend in order to process insurance quote
requests.

## IDE 

To view and edit the source code, we recommend the official Eclipse-based IDE [Spring Tool Suite](https://spring.io/tools/sts) (STS). Other IDEs might work as well, but this application has only been tested with STS.

## Installation

The Customer Self-Service backend is a [Spring Boot](https://projects.spring.io/spring-boot/) application and its dependencies are managed with [Apache Maven](https://maven.apache.org/). To get started, install Java and Maven:

1. Make sure you have [Java 8 or higher](https://adoptium.net/) installed.
2. Install Maven (see [https://maven.apache.org](https://maven.apache.org) for installation instructions). Note that most IDEs, such as the Spring Tool Suite, already contain a bundled copy of Maven. If this project is only built and launched from within the IDE, this step can be skipped.

## Launch Application

In order to launch the Customer Self-Service backend, you can run the command `mvn spring-boot:run`. When startup is completed, the output should look like this:

```
2018-05-07 14:21:13.011  INFO 12773 --- [  restartedMain] c.l.customerselfservice.DataLoader              : DataLoader has successfully imported all application dummy data, the application is now ready.
``` 

Alternatively, you can download and install the Spring Tool Suite: <!-- Above we say that we recommend STS as IDE, partial contradiction? -->

1. Install Spring Tool Suite (you can download the IDE from [https://spring.io/tools/sts](https://spring.io/tools/sts))
2. Start Spring Tool Suite and create a new workspace or open an existing one
3. Import the project:<br>
      1. Go to File -> Import -> Maven -> Existing Maven Projects
      2. Select the `LakesideMutual` repository as the root directory
      3. Enable the check mark for the `customer-self-service-backend` project
      4. Click *Finish* to import the project
4. Right-click on the project and select Run As -> Spring Boot App to start the application

By default, the Spring Boot application starts on port 8080. If this port is already used by a different application, you can change it in the 
`src/main/resources/application.properties` file.

Warnings about a `java.net.ConnectException: Connection refused` can safely be ignored. See the [FAQ](../FAQ.md#im-getting-a-connection-refused-connect-exception-on-startup) for details.

## Logical Layers
The packages follow the conventions/recommendations of the original DDD book regarding technical layering:

* The presentation logic/layer can be found under `interfaces`. 
* There are `application` and `domain` packages that contain business logic (the actual domain model and its usage).
* Data access and technical utilities can be found in `infrastructure`.

## Springdoc Open API documentation
[Springdoc](https://springdoc.org/) is an automated JSON API documentation tool for APIs built with Spring.
To access the documentation for the Customer Self-Service backend, go to the Swagger Web UI available at [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html). The native Swagger file is available at [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs).

## Testing
To run the automated tests for the Customer Self-Service backend, right-click on the project in the Spring Tool 
Suite and then click on `Run As -> JUnit Test`. The test classes are located in the `src/test/java` folder.

## Enabling Persistence
By default, the database will be re-created when the application is started and any changes that were made are lost. This is configured in `src/main/resources/application.properties` by the following setting:

```
spring.jpa.hibernate.ddl-auto=create-drop
```
If you want to persist your changes across restarts, change the setting to:
```
spring.jpa.hibernate.ddl-auto=update
```

<!-- ZIO4STX: pls review this text and the implementation. Feel free to revise. Thanks! -->

## Circuit Breaker and JMX MBean (Demo/Proof-of-Concept)
The Customer Self-Service backend retrieves customer master data from the Customer Core service. The outgoing HTTP connection is protected by a simple Hystrix circuit breaker; dummy data is returned during connection problems.

The behavior of the connection and the `@HystrixCommand` can be observed via a simple JMX MBean; to simplify matters, the remote service class serves as the implementation of the MBean interface. To access the MBean, any JMX client can be used, for instance the command-line tool `JConsole` or the Spring Boot Admin appliation (see below).

## Spring Boot Admin
The application is configured to connect to the Spring Boot Admin on startup. See the [README](../spring-boot-admin/README.md#how-it-works) to learn more. 

Custom MBeans are accessible via the "Wallboard": 

* Click on the hexagon for Customer Self-Service. 
* Next,  select `JVM -> JMX` on the left side of the screen. The known MBeans are then displayed on the right. Look for the entry `com.lakesidemutual.customerselfservice.infrastructure` and click on it to display the `customerCoreService` MBean.
