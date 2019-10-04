# ![Lakeside Mutual Logo](./resources/logo-32x32.png) Frequently Asked Questions and Troubleshooting

## Can I use IntelliJ IDEA instead of the Spring Tools Suite?

When using IntelliJ IDEA, you can just open the Maven projects individually. Alternatively, you can also create a single workspace combining all the Maven projects:

![Open Maven Projects in IntelliJ IDEA](./resources/intellij-import-instructions.png)

## Which Spring anotations are used and what do they do? 

We plan to provide a table that gives an overview here, with links to API doc as well as a tutorial that explains how they work (future work).

## How can I make sure that the backend applications are running?

You can send a test request to the application (or use the [Spring Boot Admin application](spring-boot-admin/README.md)). To check the Customer Core backend, the following request

```
curl --header 'Authorization: Bearer b318ad736c6c844b' http://localhost:8110/customers?limit=1
```

should return a customer:
```
{
  "filter" : "",
  "limit" : 1,
  "offset" : 0,
  "size" : 50,
  "customers" : [ {
    "customerId" : "bunlo9vk5f",
    "firstname" : "Ado",
    "lastname" : "Kinnett",
...
```

To check the Policy Management backend, the following request

```
curl http://localhost:8090/policies?limit=1
```

should return a policy:
```
{
  "limit" : 1,
  "offset" : 0,
  "size" : 1,
  "policies" : [ {
    "policyId" : "xtukhndm1i",
    "customer" : "rgpp0wkpec",
    "creationDate" : "2019-02-13T12:59:45.045+0000",
...
```

All Spring Boot backends also offer an `/actuator/health` endpoint that returns whether the service is UP or not:

```
curl http://localhost:8110/actuator/health 

{
  "status" : "UP"
}                         
```

## I'm getting a Connection refused: connect exception on startup 

Don't worry if you're getting an exception about a refused connection on startup:

```
2018-11-16 13:31:08.492 WARN 1592 --- [gistrationTask1] d.c.b.a.c.r.ApplicationRegistrator : Failed to register application as Application(name=Customer Self-Service Backend, managementUrl=http://localhost:8080/actuator, healthUrl=http://localhost:8080/actuator/health, serviceUrl=http://localhost:8080/, metadata={startup=2018-11-16T13:31:02.779+01:00}) at spring-boot-admin ([http://localhost:9000/instances]): I/O error on POST request for "http://localhost:9000/instances": Connection refused: connect; nested exception is java.net.ConnectException: Connection refused: connect. Further attempts are logged on DEBUG level
```

This just means that the application was unable to connect to the [Spring Boot Admin](spring-boot-admin) application. If you haven't started the Spring Boot Admin, the warning can be safely ignored.

## Why aren't you using Lombok?
The DTO (data transfer object) classes require a lot of repetitive code (e.g., getters, setters, code to map between entities and DTOs, etc).
We could use a code generator like [Lombok](https://projectlombok.org/) to get rid of this boilerplate. However, we decided against using a tool
like this, because they usually require additional IDE plug-ins which complicates the initial setup process.
