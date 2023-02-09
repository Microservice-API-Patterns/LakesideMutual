# ![Lakeside Mutual Logo](../resources/logo-32x32.png) Lakeside Mutual: Policy Management Backend

The Policy Management backend provides an HTTP resource API for the Customer Self-Service frontend and the Policy Management frontend. It also sends change events to
the Risk-Management server through an [ActiveMQ](http://activemq.apache.org/) message queue. Additionally, there are multiple message queues that are used to communicate
with the Customer Self-Service backend in order to handle insurance quote requests.

## IDE

To view and edit the source code, we recommend the official Eclipse-based IDE [Spring Tool Suite](https://spring.io/tools/sts) (STS). Other IDEs might work as well, but this application has only been tested with STS.

## Installation

The Policy Management backend is a [Spring Boot](https://projects.spring.io/spring-boot/) application and its dependencies are managed with [Apache Maven](https://maven.apache.org/). To get started, install Java and Maven:

1. Make sure you have [Java 8 or higher](https://adoptium.net/) installed.
2. Install Maven (see [https://maven.apache.org](https://maven.apache.org) for installation instructions). Note that most IDEs, such as the Spring Tool Suite, already contain a bundled copy of Maven. If this project is only built and launched from within the IDE, this step can be skipped.

## Launch Application

In order to launch the Policy Management backend, you can run the command `mvn spring-boot:run`. When startup is completed, the output should look like this:

```
2018-05-07 14:21:13.011  INFO 12773 --- [  restartedMain] c.l.policymanagement.DataLoader              : DataLoader has successfully imported all application dummy data, the application is now ready.
``` 

Alternatively, you can download and install the Spring Tool Suite:

1. Install Spring Tool Suite (you can download the IDE from [https://spring.io/tools/sts](https://spring.io/tools/sts))
2. Start Spring Tool Suite and create a new workspace or open an existing one
3. Import the project:<br>
      1. Go to File -> Import -> Maven -> Existing Maven Projects
      2. Select the `LakesideMutual` repository as the root directory
      3. Enable the check mark for the `policy-management-backend` project
      4. Click *Finish* to import the project
4. Right-click on the project and select Run As -> Spring Boot App to start the application

By default, the Spring Boot application starts on port 8090. Additionally, it provides an [ActiveMQ](http://activemq.apache.org/) message queue on port 61613.

If any of these ports is already used by a different application, you can change it in the `src/main/resources/application.properties` file.

Warnings about a `java.net.ConnectException: Connection refused` can safely be ignored. See the [FAQ](../FAQ.md#im-getting-a-connection-refused-connect-exception-on-startup) for details.

## Springdoc Open API documentation
[Springdoc](https://springdoc.org/) is an automated JSON API documentation tool for APIs built with Spring.
To access the documentation for the Policy Management backend, go to [http://localhost:8090/swagger-ui/index.html](http://localhost:8090/swagger-ui/index.html). The native Swagger file is available at [http://localhost:8090/v3/api-docs](http://localhost:8090/v3/api-docs).

## Testing
To run the automated tests for the Policy Management backend, right-click on the project in the Spring Tool 
Suite and then click on `Run As -> JUnit Test`. The test classes are located in the `src/test/java` folder.

## Logging
The Policy Management Backend uses [SLF4J (Simple Logging Facade for Java)](https://www.slf4j.org) to write logs to the console. SLF4J loggers have various log levels to denote
the importance of a log entry. For example, `ERROR` log entries are considered more important than `WARN` log entries which in turn are more important than `INFO` log entries.
You can configure the granularity of the log output by setting the root level in the `src/main/resources/logback.xml` file. By default it is set to `INFO` which means
that `DEBUG` log entries are not shown. Alternatively, you could change it to `DEBUG` to see all log output or you could change it to `WARN` to hide the `INFO` log entries
and only show warnings and errors.

The Policy Management Backend includes a `RequestTracingFilter` class which generates a random request ID for each HTTP request. This request ID is shown in the log output and it
can be used to correlate different log entries with each other. For example, when you delete a policy you get log output that looks like this:

```
2019-10-04 09:04:49.695 [http-nio-8090-exec-8] INFO 3208 c.l.p.i.PolicyInformationHolder - Creating a new policy for customer with id 'bunlo9vk5f' 
2019-10-04 09:04:49.746 [http-nio-8090-exec-8] INFO 3208 c.l.p.i.RiskManagementMessageProducer - Successfully sent a policy event to the risk management message queue. 
```

In this example, the number `3208` is the random request ID which means that both log entries were caused by the same HTTP request. From the log output we can see that
a new policy is created in the `PolicyInformationHolder` and then the `RiskManagementMessageProducer` sends a message to the Risk Management server via a message queue
in order to notify it about that new policy.

## Enabling Persistence
By default, the database will be re-created when the application is started and any changes that were made are lost. This is configured in `src/main/resources/application.properties` by the following setting:

```
spring.jpa.hibernate.ddl-auto=create-drop
```
If you want to persist your changes across restarts, change the setting to:
```
spring.jpa.hibernate.ddl-auto=update
```

## Spring Boot Admin
The application is configured to connect to the Spring Boot Admin on startup. See the [README](../spring-boot-admin/README.md#how-it-works) to learn more.