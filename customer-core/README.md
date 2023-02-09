# ![Lakeside Mutual Logo](../resources/logo-32x32.png) Lakeside Mutual: Customer Core
The Customer Core manages the personal data about the individual customers (e.g., name, email, current address, etc.). It provides this data to the other components
through an HTTP resource API.

## IDE 

To view and edit the source code, we recommend the official Eclipse-based IDE [Spring Tool Suite](https://spring.io/tools/sts) (STS). Other IDEs might work as well, but this application has only been tested with STS.

## Installation

The Customer Core is a [Spring Boot](https://projects.spring.io/spring-boot/) application and its dependencies are managed
with [Apache Maven](https://maven.apache.org/). To get started, install Java and Maven:

1. Make sure you have [Java 8 or higher](https://adoptium.net/) installed.
2. Install Maven (see [https://maven.apache.org](https://maven.apache.org) for installation instructions). Note that most IDEs, such as the Spring Tool Suite, already contain a bundled copy of Maven. If this project is only built and launched from within the IDE, this step can be skipped.

## Launch Application

In order to launch the Customer Core, you can run the command `mvn spring-boot:run`. When startup is completed, the output should look like this:

```
2018-11-12 09:23:41.117  INFO 7091 --- [  restartedMain] c.l.c.CustomerCoreApplication            : --- Customer Core started ---
``` 

Alternatively, you can download and install the Spring Tool Suite:

1. Install Spring Tool Suite (you can download the IDE from [https://spring.io/tools/sts](https://spring.io/tools/sts))
2. Start Spring Tool Suite and create a new workspace or open an existing one
3. Import the project:<br>
      1. Go to File -> Import -> Maven -> Existing Maven Projects
      2. Select the `LakesideMutual` repository as the root directory
      3. Enable the check mark for the `customer-core` project
      4. Click *Finish* to import the project
4. Right-click on the project and select Run As -> Spring Boot App to start the application

By default, the Spring Boot application starts on port 8110. If this port is already used by a different application, you can change it in the 
`src/main/resources/application.properties` file.

Warnings about a `java.net.ConnectException: Connection refused` can safely be ignored. See the [FAQ](../FAQ.md#im-getting-a-connection-refused-connect-exception-on-startup) for details.

## Springdoc Open API documentation
[Springdoc](https://springdoc.org/) is an automated JSON API documentation tool for APIs built with Spring. 
To access the documentation for the Customer Core, go to [http://localhost:8110/swagger-ui/index.html](http://localhost:8110/swagger-ui/index.html). The native Swagger file is available at [http://localhost:8110/v3/api-docs](http://localhost:8110/v3/api-docs).

## Testing
To run the automated tests for the Customer Core, right-click on the project in the Spring Tool 
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
