#/bin/sh

npx recursive-install

npx concurrently \
  --kill-others \
  --names "\
eureka-server,\
customer-core,\
customer-self-service-backend,\
customer-self-service-frontend,\
customer-management-backend,\
customer-management-frontend,\
policy-management-backend,\
policy-management-frontend,\
risk-management-server,\
spring-boot-admin" \
  --prefix-colors "\
bgRed.bold \
bgYellow.bold,\
bgBlue.bold,\
bgBlue.bold,\
bgMagenta.bold,\
bgMagenta.bold,\
bgGreen.bold,\
bgGreen.bold,\
bgWhite.bold,\
bgRed.bold" \
  \
  "cd eureka-server && mvn spring-boot:run" \
  "cd customer-core && mvn spring-boot:run" \
  "cd customer-self-service-backend && mvn spring-boot:run" \
  "cd customer-self-service-frontend && npm start"\
  "cd customer-management-backend && mvn spring-boot:run" \
  "cd customer-management-frontend && npm start"\
  "cd policy-management-backend && mvn spring-boot:run" \
  "cd policy-management-frontend && npm start"\
  "cd risk-management-server && npm start"\
  "cd spring-boot-admin && mvn spring-boot:run" 