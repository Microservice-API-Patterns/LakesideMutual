FROM openjdk:17-slim-buster as build

COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .

RUN ./mvnw -B dependency:go-offline                          

COPY src src

RUN ./mvnw -B package                                        

FROM openjdk:17-slim-buster

COPY --from=build target/policy-management-backend-0.0.1-SNAPSHOT.jar .

EXPOSE 8090
EXPOSE 61613
EXPOSE 61616

ENTRYPOINT ["java", "-jar", "policy-management-backend-0.0.1-SNAPSHOT.jar"]
