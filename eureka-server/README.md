# ![Lakeside Mutual Logo](../resources/logo-32x32.png) Lakeside Mutual: Eureka Server

Eureka Server provides a service registry. For details, see the [project's GitHub page](https://github.com/Netflix/eureka/wiki/Eureka-at-a-glance). By default, the services of this project don't use Eureka to reduce the complexity of the deployment (one less service that needs to be started).

## Installation

Eureka Server is a [Spring Boot](https://projects.spring.io/spring-boot/) application and its dependencies are managed
with [Apache Maven](https://maven.apache.org/). To get started, install Java and Maven:

1. Make sure you have [Java 8 or higher](https://adoptium.net/) installed.
2. Install Maven (see [https://maven.apache.org](https://maven.apache.org) for installation instructions). Note that most IDEs, such as the Spring Tool Suite, already contain a bundled copy of Maven. If this project is only built and launched from within the IDE, this step can be skipped.

## Launch Application

In order to launch the Eureka Server application, you can run the command `mvn spring-boot:run` and open [http://localhost:8761](http://localhost:8761). 

Alternatively, you can download and install the Spring Tool Suite:

1. Install Spring Tool Suite (you can download the IDE from [https://spring.io/tools/sts](https://spring.io/tools/sts))
2. Start Spring Tool Suite and create a new workspace or open an existing one
3. Import the project:<br>
      1. Go to File -> Import -> Maven -> Existing Maven Projects
      2. Select the `LakesideMutual` repository as the root directory
      3. Enable the check mark for the `customer-core` project
      4. Click *Finish* to import the project
4. Right-click on the project and select Run As -> Spring Boot App to start the application

By default, the Spring Boot application starts on port 8761. If this port is already used by a different application, you can change it in the 
`src/main/resources/application.properties` file. Please note that all other backend services' `application.properties` now also need to be adjusted!

## Using Eureka

By default, the other services don't use Eureka. To enable Eureka discovery, start the other backend services using with the `SPRING_PROFILES_ACTIVE=eureka` environment variable set. For example 

```
cd customer-management-backend
SPRING_PROFILES_ACTIVE=eureka mvn spring-boot:run
```

To check whether the clients have registered successfully, visit the [Eureka Dashboard](http://localhost:8761).