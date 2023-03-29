#!/bin/bash

java -jar jacoco/lib/jacococli.jar dump --address 127.0.0.1 --port 36300 --destfile report/customercore.exec

java -jar jacoco/lib/jacococli.jar dump --address 127.0.0.1 --port 36301 --destfile report/customermanagement.exec

java -jar jacoco/lib/jacococli.jar dump --address 127.0.0.1 --port 36302 --destfile report/customerselfservice.exec

java -jar jacoco/lib/jacococli.jar dump --address 127.0.0.1 --port 36303 --destfile report/policymanagement.exec
