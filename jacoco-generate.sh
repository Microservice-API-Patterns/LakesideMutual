#/bin/sh

java -jar jacoco/lib/jacococli.jar dump --address 127.0.0.1 --port 36300 --destfile report/customercore.exec
java -jar jacoco/lib/jacococli.jar report report/customercore.exec --classfiles classdump/customercore --sourcefiles customer-core/src/main/java/ --html report/customercore

java -jar jacoco/lib/jacococli.jar dump --address 127.0.0.1 --port 36301 --destfile report/customermanagement.exec
java -jar jacoco/lib/jacococli.jar report report/customermanagement.exec --classfiles classdump/customermanagement --sourcefiles customer-management-backend/src/main/java/ --html report/customermanagement

java -jar jacoco/lib/jacococli.jar dump --address 127.0.0.1 --port 36302 --destfile report/customerselfservice.exec
java -jar jacoco/lib/jacococli.jar report report/customerselfservice.exec --classfiles classdump/customerselfservice --sourcefiles customer-self-service-backend/src/main/java/ --html report/customerselfservice

java -jar jacoco/lib/jacococli.jar dump --address 127.0.0.1 --port 36303 --destfile report/policymanagement.exec
java -jar jacoco/lib/jacococli.jar report report/policymanagement.exec --classfiles classdump/policymanagement --sourcefiles policy-management-backend/src/main/java/ --html report/policymanagement
