# ![Lakeside Mutual Logo](./resources/logo-32x32.png) Microservice API Patterns in Lakeside Mutual

A number of [Microservice API Patterns (MAP)](https://microservice-api-patterns.org/) realization examples can be found in the `interface` layers of the Java backends that implement the Lakeside Mutual microservices. 

## Responsibility Patterns

| Pattern | Example (Class) | Backend Microservice(s) |
|:------- | :-------------- | :--------------------- |
| *Endpoint roles* |||
| [Processing Resource](https://www.microservice-api-patterns.org/patterns/responsibility/endpointRoles/ProcessingResource) | [InsuranceQuoteRequestProcessingResource.java](./policy-management-backend/src/main/java/com/lakesidemutual/policymanagement/interfaces/InsuranceQuoteRequestProcessingResource.java) | Policy Management |
| [Information Holder Resource](https://www.microservice-api-patterns.org/patterns/responsibility/endpointRoles/InformationHolderResource) | [CustomerInformationHolder.java](./customer-core/src/main/java/com/lakesidemutual/customercore/interfaces/CustomerInformationHolder.java) | Customer Core |
| | [NotificationInformationHolder.java](./customer-management-backend/src/main/java/com/lakesidemutual/customermanagement/interfaces/NotificationInformationHolder.java) | Customer Management |
| | [InteractionLogInformationHolder.java](./customer-management-backend/src/main/java/com/lakesidemutual/customermanagement/interfaces/InteractionLogInformationHolder.java) | Customer Management |  
| [Master Data Holder](https://www.microservice-api-patterns.org/patterns/responsibility/informationHolderEndpointTypes/MasterDataHolder) | [CustomerInformationHolder.java](./customer-core/src/main/java/com/lakesidemutual/customercore/interfaces/CustomerInformationHolder.java) | Customer Core |
| | [PolicyInformationHolder.java](./policy-management-backend/src/main/java/com/lakesidemutual/policymanagement/interfaces/PolicyInformationHolder.java) | Policy Management |
| | [UserInformationHolder.java](./customer-self-service-backend/src/main/java/com/lakesidemutual/customerselfservice/interfaces/UserInformationHolder.java) | Customer Self Service | |
| [Operational Data Holder](https://microservice-api-patterns.org/patterns/responsibility/informationHolderEndpointTypes/OperationalDataHolder) |  [InsuranceQuoteRequestProcessingResource.java](./policy-management-backend/src/main/java/com/lakesidemutual/policymanagement/interfaces/InsuranceQuoteRequestProcessingResource.java) | Policy Management | 
| | [InsuranceQuoteRequestProcessingResource.java](./customer-self-service-backend/src/main/java/com/lakesidemutual/customerselfservice/interfaces/InsuranceQuoteRequestProcessingResource.java) | Customer Self Service | 
| [Reference Data Holder](https://www.microservice-api-patterns.org/patterns/responsibility/informationHolderEndpointTypes/ReferenceDataHolder) | [CityReferenceDataHolder.java](./customer-core/src/main/java/com/lakesidemutual/customercore/interfaces/CityReferenceDataHolder.java) | Customer Core |
| [Data Transfer Resource](https://microservice-api-patterns.org/patterns/responsibility/informationHolderEndpointTypes/DataTransferResource) | [Risk report transfer service](https://github.com/socadk/LakesideMutual/blob/map-revision-21/risk-management-server/riskmanagement.proto) (gRPC protocol buffer) | Risk Management | 
| [Link Lookup Resource](https://microservice-api-patterns.org/patterns/responsibility/informationHolderEndpointTypes/LinkLookupResource) | tba |  | 
| *Operation Responsibilities* | | |
| [Computation Function](https://www.microservice-api-patterns.org/patterns/responsibility/operationResponsibilities/ComputationFunction) | `computeRiskFactor` in [RiskComputationService.java](./policy-management-backend/src/main/java/com/lakesidemutual/policymanagement/interfaces/RiskComputationService.java) | Policy Management |
| [Retrieval Operation](https://microservice-api-patterns.org/patterns/responsibility/operationResponsibilities/RetrievalOperation) | `getInsuranceQuoteRequests` in [InsuranceQuoteRequestProcessingResource.java](./customer-self-service-backend/src/main/java/com/lakesidemutual/customerselfservice/interfaces/InsuranceQuoteRequestProcessingResource.java) | Customer Self Service, Policy Management |
| [State Creation Operation](https://microservice-api-patterns.org/patterns/responsibility/operationResponsibilities/StateCreationOperation) | `createInsuranceQuoteRequest` in [InsuranceQuoteRequestProcessingResource.java](./customer-self-service-backend/src/main/java/com/lakesidemutual/customerselfservice/interfaces/InsuranceQuoteRequestProcessingResource.java) | Customer Self Service |
| [State Transition Operation](https://microservice-api-patterns.org/patterns/responsibility/operationResponsibilities/StateTransitionOperation) | `respondToInsuranceQuote` in [InsuranceQuoteRequestProcessingResource.java](./customer-self-service-backend/src/main/java/com/lakesidemutual/customerselfservice/interfaces/InsuranceQuoteRequestProcessingResource.java) | Customer Self Service, Policy Management |

<!-- tba variants of operation responsibilities -->

## Quality Patterns

| Pattern | Example (Class) | Backend Microservice(s) |
|:---------- | :------------------------- | :---------------- |
| *Quality Management and Governance* |  | | 
| [API Key](https://www.microservice-api-patterns.org/patterns/quality/qualityManagementAndGovernance/APIKey) | [APIKeyAuthenticationManager.java](./customer-core/src/main/java/com/lakesidemutual/customercore/interfaces/configuration/APIKeyAuthenticationManager.java) | Customer Core |
| [Rate Limit](https://www.microservice-api-patterns.org/patterns/quality/qualityManagementAndGovernance/RateLimit) | [RateLimitInterceptor.java](./customer-self-service-backend/src/main/java/com/lakesidemutual/customerselfservice/interfaces/configuration/RateLimitInterceptor.java) | Customer Self Service |
| [Error Report](https://www.microservice-api-patterns.org/patterns/quality/qualityManagementAndGovernance/ErrorReport) | [ErrorController.java](./customer-core/src/main/java/com/lakesidemutual/customercore/interfaces/ErrorController.java) | Customer Core |
| | [ErrorController.java](./customer-management-backend/src/main/java/com/lakesidemutual/customermanagement/interfaces/ErrorController.java) | Customer Management |
| | [ErrorController.java](./customer-self-service-backend/src/main/java/com/lakesidemutual/customerselfservice/interfaces/ErrorController.java) | Customer Self Service |
| | [ErrorController.java](./policy-management-backend/src/main/java/com/lakesidemutual/policymanagement/interfaces/ErrorController.java) | Policy Management |
| | [CustomerInformationHolder.java](./policy-management-backend/src/main/java/com/lakesidemutual/policymanagement/interfaces/CustomerInformationHolder.java) | Policy Management |
| *Data Transfer Parsimony* |  | | 
| [Wish List](https://www.microservice-api-patterns.org/patterns/quality/dataTransferParsimony/WishList) | [CustomerInformationHolder.java](./customer-core/src/main/java/com/lakesidemutual/customercore/interfaces/CustomerInformationHolder.java) | Customer Core |
| | [PolicyInformationHolder.java](./policy-management-backend/src/main/java/com/lakesidemutual/policymanagement/interfaces/PolicyInformationHolder.java) | Policy Management |
| [Wish Template](https://www.microservice-api-patterns.org/patterns/quality/dataTransferParsimony/WishTemplate) | tba |  |
| [Request Bundle](https://www.microservice-api-patterns.org/patterns/quality/dataTransferParsimony/RequestBundle) | [CustomerInformationHolder.java](./customer-core/src/main/java/com/lakesidemutual/customercore/interfaces/CustomerInformationHolder.java) | Customer Core |
| [Conditional Request](https://microservice-api-patterns.org/patterns/quality/dataTransferParsimony/ConditionalRequest) | tba |  | 
| *Reference Management* |  | | 
| [Embedded Entity](https://www.microservice-api-patterns.org/patterns/quality/referenceManagement/EmbeddedEntity) | `customerProfile` in [CustomerDto.java](./customer-management-backend/src/main/java/com/lakesidemutual/customermanagement/interfaces/dtos/CustomerDto.java) | e.g. Customer Management | 
| [Linked Information Holder](https://www.microservice-api-patterns.org/patterns/quality/referenceManagement/LinkedInformationHolder) | [CustomerInformationHolder.java](./policy-management-backend/src/main/java/com/lakesidemutual/policymanagement/interfaces/CustomerInformationHolder.java) | Policy Management |


## Structure Patterns

| Pattern | Example (Class) | Backend Microservice(s) |
|:------- | :-------------- | :----------------- |
| [Atomic Parameter](https://www.microservice-api-patterns.org/patterns/structure/representationElements/AtomicParameter) | [CitiesResponseDto.java](./customer-core/src/main/java/com/lakesidemutual/customercore/interfaces/dtos/city/CitiesResponseDto.java) | Customer Core |
| [Atomic Parameter List](https://www.microservice-api-patterns.org/patterns/structure/representationElements/AtomicParameterList) | [AddressDto.java](./customer-core/src/main/java/com/lakesidemutual/customercore/interfaces/dtos/customer/AddressDto.java) | Customer Core |
| [Parameter Tree](https://www.microservice-api-patterns.org/patterns/structure/representationElements/ParameterTree) | [CustomerResponseDto.java](./customer-core/src/main/java/com/lakesidemutual/customercore/interfaces/dtos/customer/CustomerResponseDto.java) | Customer Core |
| [Parameter Forest](https://www.microservice-api-patterns.org/patterns/structure/representationElements/ParameterForest) | [CustomersResponseDto.java](./customer-core/src/main/java/com/lakesidemutual/customercore/interfaces/dtos/customer/CustomersResponseDto.java) | Customer Core |
| [Pagination](https://www.microservice-api-patterns.org/patterns/structure/compositeRepresentations/Pagination) | [CustomerInformationHolder.java](./customer-core/src/main/java/com/lakesidemutual/customercore/interfaces/CustomerInformationHolder.java) | Customer Core |
| | [PaginatedCustomerResponseDto.java](./customer-core/src/main/java/com/lakesidemutual/customercore/interfaces/dtos/customer/PaginatedCustomerResponseDto.java) | Customer Core |
| | [PaginatedPolicyResponseDto.java](./policy-management-backend/src/main/java/com/lakesidemutual/policymanagement/interfaces/dtos/policy/PaginatedPolicyResponseDto.java) | Policy Management |
| [Context Representation](https://microservice-api-patterns.org/patterns/structure/compositeRepresentations/ContextRepresentation) | tba | | 
| *Element Stereotypes* | | | 
| [Data Element](https://microservice-api-patterns.org/patterns/structure/elementStereotypes/DataElement) | Data Transfer Objects (DTOs) in `*.interfaces.dtos.*` packages | All microservices | 
| [Id Element](https://microservice-api-patterns.org/patterns/structure/elementStereotypes/IdElement) | `customerId` in [CustomerDto.java](./customer-management-backend/src/main/java/com/lakesidemutual/customermanagement/interfaces/dtos/CustomerDto.java) | Customer Management, other microservices | 
| [Link Element](https://microservice-api-patterns.org/patterns/structure/elementStereotypes/LinkElement) | Pagination links in `createPaginatedCustomerResponseDto` in [CustomerInformationHolder.java](./customer-core/src/main/java/com/lakesidemutual/customercore/interfaces/CustomerInformationHolder.java) | Customer Core | 
| [Metadata Element](https://microservice-api-patterns.org/patterns/structure/elementStereotypes/MetadataElement) | Control variant: instances of Pagination (e.g., [PaginatedCustomerResponseDto.java](./customer-core/src/main/java/com/lakesidemutual/customercore/interfaces/dtos/customer/PaginatedCustomerResponseDto.java)) |  Customer Core |  | 
|  | Aggregation variant: counts and averages in risk reports (CSV) | Risk Management |
|  | Provenance variant: tba |  |

Note that some classes appear in several projects; microservices are not supposed to share code libraries because that would break their independent deployability, one of the defining [microservices tenets](https://medium.com/olzzio/seven-microservices-tenets-e97d6b0990a4).


# What are Microservices? Why Patterns?

Some answers to these questions can be found on the [MAP website](https://microservice-api-patterns.org/primer).

<!-- Other Patterns: PoEAA, EIP, GoF? -->
