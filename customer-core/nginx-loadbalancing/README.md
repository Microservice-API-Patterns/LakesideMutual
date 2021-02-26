# Loadbalacing using NGINX

This demo shows how NGINX can be used to load-balance between multiple instances of the customer-core application. The demo reuses the [Dockerfile](../Dockerfile) of the Customer Core application and combines it with a simple NGINX container that balances the load among four app instances:

```
http {
    sendfile on;
 
    upstream app_servers {
        server nginx-loadbalancing_app_1:8110;
        server nginx-loadbalancing_app_2:8110;
        server nginx-loadbalancing_app_3:8110;
        server nginx-loadbalancing_app_4:8110;
    }
    ...
```

The [docker-compose.yml](./docker-compose.yml) links the applications together.

## Prerequisites

This demo requires Docker and docker-compose to be installed.

## Building

To build the Docker images, run

```
docker-compose build
```

## Running

To run the load-balanced application, run

```
docker-compose up --scale app=4
```

This tells docker-compose to scale the app (the Customer Core) to four instances.

## Testing

When a request is sent to the proxy:

```
curl --header 'Authorization: Bearer b318ad736c6c844b' http://localhost/customers\?limit\=1
```
one of the application instances will receive it. This can be seen from the log output:

```
proxy_1  | 172.18.0.1 - - [13/Feb/2019:14:50:35 +0000] "GET /customers?limit=1 HTTP/1.1" 200 856 "-" "curl/7.63.0"
app_4    | 2019-02-13 14:50:35.420 DEBUG 1 --- [nio-8110-exec-1] o.s.w.f.CommonsRequestLoggingFilter      : After request [uri=/customers?limit=1;headers={host=[localhost], x-real-ip=[172.18.0.1], x-forwarded-for=[172.18.0.1], connection=[close], user-agent=[curl/7.63.0], accept=[*/*], authorization=[Bearer b318ad736c
```

We can see that the `app_4` instance handled the request. The log also shows the additional request headers inserted by the proxy. 

```
headers={host=[localhost], x-real-ip=[172.18.0.1], x-forwarded-for=[172.18.0.1], ...
```

These could be used by the application to for example determine the IP address of the proxy.

## Testing Using Apache Bench

To put more load on the applications, the [Apache Bench](https://httpd.apache.org/docs/2.4/programs/ab.html) tool can be used to make multiple requests:

```
ab -n 100 -H 'Authorization: Bearer b318ad736c6c844b' http://localhost/customers\?limit\=1
```

Take a look at the log output to see that the requests are distributed among the four app instances.

## Killing Instances

When `app` instances are stopped

```
docker stop nginx-loadbalancing_app_3 nginx-loadbalancing_app_4 
```
```
app_3    | 2019-02-13 14:56:55.839  INFO 1 --- [       Thread-6] o.s.s.c.ThreadPoolTaskScheduler          : Shutting down ExecutorService
app_3    | 2019-02-13 14:56:55.845  INFO 1 --- [       Thread-6] j.LocalContainerEntityManagerFactoryBean : Closing JPA EntityManagerFactory for persistence unit 'default'
app_3    | 2019-02-13 14:56:55.845  INFO 1 --- [       Thread-6] .SchemaDropperImpl$DelayedDropActionImpl : HHH000477: Starting delayed evictData of schema as part of SessionFactory shut-down'
app_3    | 2019-02-13 14:56:55.872  INFO 1 --- [       Thread-6] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown initiated...
app_3    | 2019-02-13 14:56:55.879  INFO 1 --- [       Thread-6] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown completed.
nginx-loadbalancing_app_3 exited with code 143
app_4    | 2019-02-13 14:57:05.002  INFO 1 --- [       Thread-6] o.s.s.c.ThreadPoolTaskScheduler          : Shutting down ExecutorService
app_4    | 2019-02-13 14:57:05.008  INFO 1 --- [       Thread-6] j.LocalContainerEntityManagerFactoryBean : Closing JPA EntityManagerFactory for persistence unit 'default'
app_4    | 2019-02-13 14:57:05.009  INFO 1 --- [       Thread-6] .SchemaDropperImpl$DelayedDropActionImpl : HHH000477: Starting delayed evictData of schema as part of SessionFactory shut-down'
app_4    | 2019-02-13 14:57:05.037  INFO 1 --- [       Thread-6] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown initiated...
app_4    | 2019-02-13 14:57:05.044  INFO 1 --- [       Thread-6] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown completed.
nginx-loadbalancing_app_4 exited with code 143
```

NGINX will notice this and send requests to the remaining instances:

```
proxy_1  | 2019/02/13 15:07:03 [error] 6#6: *5 connect() failed (113: No route to host) while connecting to upstream, client: 172.18.0.1, server: , request: "GET /customers?limit=1 HTTP/1.0", upstream: "http://172.18.0.3:8110/customers?limit=1", host: "localhost"
proxy_1  | 2019/02/13 15:07:06 [error] 6#6: *5 connect() failed (113: No route to host) while connecting to upstream, client: 172.18.0.1, server: , request: "GET /customers?limit=1 HTTP/1.0", upstream: "http://172.18.0.4:8110/customers?limit=1", host: "localhost"
```



## A Note on Persistence

Please note that in the default configuration of the Customer Core, the database is stored in a local file. This means that each application instance has its own database. In a real world scenario, a proper database would have to be used instead.