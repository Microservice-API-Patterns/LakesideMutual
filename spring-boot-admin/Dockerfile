FROM maven:3.5.2-jdk-8 AS build
ARG BASE=/usr/src/app
COPY pom.xml ${BASE}/
COPY src ${BASE}/src
RUN mvn -Dmaven.gitcommitid.skip=true -f ${BASE}/pom.xml install

FROM openjdk:8-jdk-alpine
COPY --from=build /usr/src/app/target/dependency/BOOT-INF/lib/* /app/lib/
COPY --from=build /usr/src/app/target/dependency/META-INF /app/META-INF
COPY --from=build /usr/src/app/target/dependency/BOOT-INF/classes /app

EXPOSE 9000
ENTRYPOINT ["java","-cp","app:app/lib/*","com.lakesidemutual.springbootadmin.SpringBootAdminApplication"]
