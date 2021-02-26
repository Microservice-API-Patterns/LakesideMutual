package com.lakesidemutual.customermanagement.tests;

import static com.tngtech.archunit.core.domain.JavaClass.Predicates.implement;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

import org.junit.runner.RunWith;
import org.microserviceapipatterns.domaindrivendesign.Aggregate;
import org.microserviceapipatterns.domaindrivendesign.DomainEvent;
import org.microserviceapipatterns.domaindrivendesign.DomainService;
import org.microserviceapipatterns.domaindrivendesign.Entity;
import org.microserviceapipatterns.domaindrivendesign.EntityIdentifier;
import org.microserviceapipatterns.domaindrivendesign.Factory;
import org.microserviceapipatterns.domaindrivendesign.Repository;
import org.microserviceapipatterns.domaindrivendesign.RootEntity;
import org.microserviceapipatterns.domaindrivendesign.ValueObject;
import org.springframework.web.bind.annotation.RestController;

import com.tngtech.archunit.core.importer.ImportOption.DontIncludeTests;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.junit.ArchUnitRunner;
import com.tngtech.archunit.lang.ArchRule;

@RunWith(ArchUnitRunner.class)
@AnalyzeClasses(
		packages = {"com.lakesidemutual.customermanagement","org.microserviceapipatterns.domaindrivendesign"},
		importOptions = {DontIncludeTests.class}
		)
public class ArchitectureTests {
	private final static String ROOT_PKG = "com.lakesidemutual.customermanagement..";
	private final static String INTERFACES_PKG = "com.lakesidemutual.customermanagement.interfaces..";
	private final static String APPLICATION_PKG = "com.lakesidemutual.customermanagement.application..";
	private final static String DOMAIN_PKG = "com.lakesidemutual.customermanagement.domain..";
	private final static String INFRASTRUCTURE_PKG = "com.lakesidemutual.customermanagement.infrastructure..";
	private final static String DTOS_PKG = "com.lakesidemutual.customermanagement.interfaces.dtos..";
	private final static String INTERFACES_LAYER = "Interfaces";
	private final static String APPLICATION_LAYER = "Application";
	private final static String DOMAIN_LAYER = "Domain";
	private final static String INFRASTRUCTURE_LAYER = "Infrastructure";

	@ArchTest
	public static final ArchRule dtos_should_reside_in_dtos_pkg = classes()
	.that().haveNameMatching(".*Dto$")
	.should().resideInAPackage(DTOS_PKG);

	@ArchTest
	public static final ArchRule rest_controllers_should_reside_in_interfaces_pkg = classes()
	.that().areAnnotatedWith(RestController.class)
	.should().resideInAPackage(INTERFACES_PKG);

	@ArchTest
	public static final ArchRule ddd_objects_should_reside_in_domain_pkg = classes()
	.that().resideInAPackage(ROOT_PKG).and(
			implement(Aggregate.class)
			.or(implement(DomainEvent.class))
			.or(implement(DomainService.class))
			.or(implement(Entity.class))
			.or(implement(EntityIdentifier.class))
			.or(implement(Factory.class))
			.or(implement(RootEntity.class))
			.or(implement(ValueObject.class))
			)
	.should().resideInAPackage(DOMAIN_PKG);

	@ArchTest
	public static final ArchRule layer_dependencies_are_respected = layeredArchitecture()
	.layer(INTERFACES_LAYER).definedBy(INTERFACES_PKG)
	.layer(APPLICATION_LAYER).definedBy(APPLICATION_PKG)
	.layer(DOMAIN_LAYER).definedBy(DOMAIN_PKG)
	.layer(INFRASTRUCTURE_LAYER).definedBy(INFRASTRUCTURE_PKG)
	// .whereLayer(INTERFACES_LAYER).mayNotBeAccessedByAnyLayer()
	.whereLayer(APPLICATION_LAYER).mayOnlyBeAccessedByLayers(INTERFACES_LAYER)
	.whereLayer(DOMAIN_LAYER).mayOnlyBeAccessedByLayers(INTERFACES_LAYER, APPLICATION_LAYER, INFRASTRUCTURE_LAYER)
	.whereLayer(INFRASTRUCTURE_LAYER).mayOnlyBeAccessedByLayers(INTERFACES_LAYER, APPLICATION_LAYER, DOMAIN_LAYER);

	@ArchTest
	public static ArchRule entities_should_be_suffixed =
	classes()
	.that().resideInAPackage(ROOT_PKG)
	.and().implement(Entity.class)
	.and().dontImplement(RootEntity.class)
	.should().haveSimpleNameEndingWith("Entity");

	@ArchTest
	public static ArchRule root_entities_should_be_suffixed =
	classes()
	.that().resideInAPackage(ROOT_PKG)
	.and().implement(RootEntity.class)
	.should().haveSimpleNameEndingWith("AggregateRoot");

	@ArchTest
	public static ArchRule repositories_should_be_suffixed =
	classes()
	.that().resideInAPackage(ROOT_PKG)
	.and().implement(Repository.class)
	.should().haveSimpleNameEndingWith("Repository");

	@ArchTest
	public static ArchRule factories_should_be_suffixed =
	classes()
	.that().resideInAPackage(ROOT_PKG)
	.and().implement(Factory.class)
	.should().haveSimpleNameEndingWith("Factory");

	@ArchTest
	public static ArchRule domain_events_should_be_suffixed =
	classes()
	.that().resideInAPackage(ROOT_PKG)
	.and().implement(DomainEvent.class)
	.should().haveSimpleNameEndingWith("Event");

	@ArchTest
	public static final ArchRule rest_controllers_should_be_suffixed = classes()
	.that().areAnnotatedWith(RestController.class)
	.should().haveSimpleNameEndingWith("Controller")
	.orShould().haveSimpleNameEndingWith("DataHolder")
	.orShould().haveSimpleNameEndingWith("ComputationService")
	.orShould().haveSimpleNameEndingWith("InformationHolder");
}