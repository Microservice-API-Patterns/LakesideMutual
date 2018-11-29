# Frequently Asked Questions and Troubleshooting

## Which Spring anotations are used and what do they do? 

We plan to provide a table that gives an overview here, with links to API doc as well as a tutorial that explains how they work (future work).


## How can I make sure that the backend applications are running?

You can send a test request to the application (or use the [Spring Boot Admin application](spring-boot-admin/README.md)). To check the Customer Self-Service backend, the following request

```
curl http://localhost:8080/customers?limit=1
```

should return a customer:
```
{
  "limit": 1,
  "offset": 0,
  "size": 50,
  "customers": [
    {
      "customerId": "a46b5c29-0500-4ca4-92d5-ac7cdba41ad9",
      "firstname": "Max",
      "lastname": "Mustermann",
...
```

To check the Policy Management backend, the following request

```
curl http://localhost:8090/policies?limit=1
```

should return a policy:
```
{
  "limit" : 10,
  "offset" : 0,
  "size" : 1,
  "policies" : [ {
    "policyId" : "c8bcb900-8112-49b0-be02-0f030dce7002",
    "customer" : "a46b5c29-0500-4ca4-92d5-ac7cdba41ad9",
    "creationDate" : "2018-06-26T13:04:18.990+0000",
...
```

## I'm getting a Connection refused: connect exception on startup 

Don't worry if you're getting an exception about a refused connection on startup:

```
2018-11-16 13:31:08.492 WARN 1592 --- [gistrationTask1] d.c.b.a.c.r.ApplicationRegistrator : Failed to register application as Application(name=Customer Self-Service Backend, managementUrl=http://localhost:8080/actuator, healthUrl=http://localhost:8080/actuator/health, serviceUrl=http://localhost:8080/, metadata={startup=2018-11-16T13:31:02.779+01:00}) at spring-boot-admin ([http://localhost:9000/instances]): I/O error on POST request for "http://localhost:9000/instances": Connection refused: connect; nested exception is java.net.ConnectException: Connection refused: connect. Further attempts are logged on DEBUG level
```

This just means that the application was unable to connect to the [Spring Boot Admin](spring-boot-admin) application. If you haven't started the Spring Boot Admin, the warning can be safely ignored.