# ![Lakeside Mutual Logo](../resources/logo-32x32.png) Lakeside Mutual: Spring Boot Admin

The Spring Boot Admin application monitors the other sample applications and provides various insights. For details, see the [project's GitHub page](https://github.com/codecentric/spring-boot-admin).

## Installation

Spring Boot Admin is a [Spring Boot](https://projects.spring.io/spring-boot/) application and its dependencies are managed
with [Apache Maven](https://maven.apache.org/). To get started, install Java and Maven:

1. Make sure you have [Java 8 or higher](https://adoptium.net/) installed.
2. Install Maven (see [https://maven.apache.org](https://maven.apache.org) for installation instructions). Note that most IDEs, such as the Spring Tool Suite, already contain a bundled copy of Maven. If this project is only built and launched from within the IDE, this step can be skipped.

## Launch Application

In order to launch the Spring Boot Admin application, you can run the command `mvn spring-boot:run` and open [http://localhost:9000](http://localhost:9000). 

Alternatively, you can download and install the Spring Tool Suite:

1. Install Spring Tool Suite (you can download the IDE from [https://spring.io/tools/sts](https://spring.io/tools/sts))
2. Start Spring Tool Suite and create a new workspace or open an existing one
3. Import the project:<br>
      1. Go to File -> Import -> Maven -> Existing Maven Projects
      2. Select the `LakesideMutual` repository as the root directory
      3. Enable the check mark for the `customer-core` project
      4. Click *Finish* to import the project
4. Right-click on the project and select Run As -> Spring Boot App to start the application

By default, the Spring Boot application starts on port 9000. If this port is already used by a different application, you can change it in the 
`src/main/resources/application.properties` file.

# How it works

Spring Boot applications include an `spring-boot-admin-starter-client` that connects to the Spring Boot Admin application on startup to register themselves. This can be observed during the startup of the client:

```
... [gistrationTask1] d.c.b.a.c.r.ApplicationRegistrator: Application registered itself as 4674026262b5
```

Spring Boot Admin will then call the `/actuator` endpoints of the applications to monitor the application health. For example, the `/actuator/health` endpoints reports whether the service is up:

```
curl http://localhost:8100/actuator/health

{
  "status" : "UP"
}
```

## How it works when using Eureka

When Eureka is used, Spring Boot applications register themselves at the Eureka Server. The Spring Boot Admin uses Eureka to discover all other Spring Boot applications. The `spring-boot-admin-starter-client` mentioned above is not needed anymore.

See the [Spring Boot Admin documentation](http://codecentric.github.io/spring-boot-admin/2.1.0) to learn more about the different capabilities and configuration options.