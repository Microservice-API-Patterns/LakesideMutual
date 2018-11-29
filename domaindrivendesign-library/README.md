# Lakeside Mutual : Domain-Driven Design Library

This library is a collection of interfaces and abstract classes to express domain-driven design concepts in Java code. Most types in this library are pure marker interfaces and only for documentation purposes. An added benefit is that IDEs can show all implementations of an interface, thus making it easy to discover all implementations of for example Value Objects or Entities.


## Pattern representations 

<!-- Future work: feature Context Map with Lakeside Mutual services as Bounded Contexts and this library as a shared kernel 

```plantuml
@startuml
package LakesideMutualSampleApplication{
class DomainDrivenDesign<<boundedContext>>
class LakesideMutualSystems<<boundedContext>>
DomainDrivenDesign <-- LakesideMutualSystems : use
}
@enduml
```
--->

| Pattern (or Variant)              | Implementation in this library | Example            |
| -------------------- | ------------------------------ |------------------------- |
| Layered Architecture | n/a | Top-level packages in Lakeside Mutual backend represents the layers suggested by E. Evans in the ["light blue" book](http://dddcommunity.org/book/evans_2003/) |
| Module               | n/a  | Java sub-packages in Domain Layer of Lakeside Mutual backends |
| Entity               | [Entity](src/main/java/com/lakesidemutual/domaindrivendesign/Entity.java) | [CustomerProfileEntity](../customer-core/src/main/java/com/lakesidemutual/customercore/domain/customer/CustomerProfileEntity.java) |
| Value Object         | [ValueObject](src/main/java/com/lakesidemutual/domaindrivendesign/ValueObject.java)  | [PolicyPeriod](../policy-management-backend/src/main/java/com/lakesidemutual/policymanagement/domain/policy/PolicyPeriod.java) |
| Service              | [Service](src/main/java/com/lakesidemutual/domaindrivendesign/Service.java)  | See layer-specific variants below: |
| Application Service  | [ApplicationService](src/main/java/com/lakesidemutual/domaindrivendesign/ApplicationService.java)  | [CustomerService](../customer-core/src/main/java/com/lakesidemutual/customercore/application/CustomerService.java) |
| Domain Service       | [DomainService](src/main/java/com/lakesidemutual/domaindrivendesign/DomainService.java)  | [InteractionLogService](../customer-management-backend/src/main/java/com/lakesidemutual/customermanagement/domain/InteractionLogService.java) |
| Infrastructure Service| [InfrastructureService](src/main/java/com/lakesidemutual/domaindrivendesign/InfrastructureService.java)  | [CustomerCoreService](../customer-management-backend/src/main/java/com/lakesidemutual/customermanagement/infrastructure/CustomerCoreService.java) |
| Repository           | [Repository](src/main/java/com/lakesidemutual/domaindrivendesign/Repository.java) | [CustomerRepository](../customer-core/src/main/java/com/lakesidemutual/customercore/infrastructure/CustomerRepository.java) |
| Factory              | [Factory ](src/main/java/com/lakesidemutual/domaindrivendesign/Factory.java) | [CustomerFactory](../customer-core/src/main/java/com/lakesidemutual/customercore/domain/customer/CustomerFactory.java) |
| Aggregate            | [Aggregate](src/main/java/com/lakesidemutual/domaindrivendesign/Aggregate.java) | [CustomerAggregateRoot](../customer-core/src/main/java/com/lakesidemutual/customercore/domain/customer/CustomerAggregateRoot.java) |
| Bounded Context      | [BoundedContext](src/main/java/com/lakesidemutual/domaindrivendesign/BoundedContext.java) | CustomerCoreApplication	 |
| Context Map          | n/a | n/a	 |
| Subdomain patterns   | n/a | n/a |

<!-- Should we also feature Subdomains? tbd (see what IDDD book has to say and does in sample app) -->

## More Information
[This website](https://www.ifs.hsr.ch/index.php?id=15666&L=4) collects pointers to DDD resources, for instance:

* This [DDD reference](http://domainlanguage.com/ddd/reference/) has pattern summaries.
* The [DDD community](http://dddcommunity.org/learning-ddd/what_is_ddd/) features supporting material.
* [InfoQ](https://www.infoq.com/domain-driven-design) provides articles and presentaions around DDD.

### Other Example(s)
A [DDD Sample Application](https://github.com/citerus/dddsample-core) is available on GitHub, dealing with cargo booking, routing and tracking. It implements the running example in Eric Evans' [Domain-Driven Design](https://domainlanguage.com/ddd/).

The [agile project management sample](https://github.com/VaughnVernon/IDDD_Samples) accompanying [Implementing Domain-Driven Design](http://www.informit.com/store/implementing-domain-driven-design-9780321834577) by Vaughn Vernon can be found on GitHub as well. 
