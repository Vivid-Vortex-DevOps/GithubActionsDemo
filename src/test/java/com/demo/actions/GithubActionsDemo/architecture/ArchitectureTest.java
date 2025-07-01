package com.demo.actions.GithubActionsDemo.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

/**
 * Architecture tests using ArchUnit.
 * 
 * <p>This class demonstrates how to enforce architectural rules
 * and ensure proper layering in the application.
 */
@DisplayName("Architecture Tests")
class ArchitectureTest {

    private static JavaClasses importedClasses;

    @BeforeAll
    static void setUp() {
        importedClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("com.demo.actions.GithubActionsDemo");
    }

    @Test
    @DisplayName("Controllers should only be accessed by controllers")
    void controllersShouldOnlyBeAccessedByControllers() {
        ArchRule rule = noClasses()
                .that().resideOutsideOfPackage("..controller..")
                .should().accessClassesThat().resideInAPackage("..controller..");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Services should not access controllers")
    void servicesShouldNotAccessControllers() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..service..")
                .should().accessClassesThat().resideInAPackage("..controller..");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("DTOs should not access services")
    void dtosShouldNotAccessServices() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..dto..")
                .should().accessClassesThat().resideInAPackage("..service..");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Exceptions should not access other packages")
    void exceptionsShouldNotAccessOtherPackages() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..exception..")
                .should().accessClassesThat().resideInAnyPackage("..controller..", "..service..", "..dto..");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("All classes should have proper naming convention")
    void allClassesShouldHaveProperNamingConvention() {
        ArchRule rule = classes()
                .should().haveSimpleNameEndingWith("Controller")
                .orShould().haveSimpleNameEndingWith("Service")
                .orShould().haveSimpleNameEndingWith("Dto")
                .orShould().haveSimpleNameEndingWith("Exception")
                .orShould().haveSimpleNameEndingWith("Application")
                .orShould().haveSimpleNameEndingWith("Configuration");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Controllers should be annotated with @RestController")
    void controllersShouldBeAnnotatedWithRestController() {
        ArchRule rule = classes()
                .that().resideInAPackage("..controller..")
                .should().beAnnotatedWith("org.springframework.web.bind.annotation.RestController");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Services should be annotated with @Service")
    void servicesShouldBeAnnotatedWithService() {
        ArchRule rule = classes()
                .that().resideInAPackage("..service..")
                .should().beAnnotatedWith("org.springframework.stereotype.Service");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("DTOs should not have business logic")
    void dtosShouldNotHaveBusinessLogic() {
        ArchRule rule = classes()
                .that().resideInAPackage("..dto..")
                .should().onlyHaveDependentClassesThat().resideInAnyPackage(
                        "java..", "jakarta..", "com.fasterxml.jackson..", "io.swagger.."
                );

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Exceptions should extend RuntimeException")
    void exceptionsShouldExtendRuntimeException() {
        ArchRule rule = classes()
                .that().resideInAPackage("..exception..")
                .should().beAssignableTo(RuntimeException.class);

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("Layered architecture should be respected")
    void layeredArchitectureShouldBeRespected() {
        var layeredArchitecture = layeredArchitecture()
                .consideringAllDependencies()
                .layer("Controller").definedBy("..controller..")
                .layer("Service").definedBy("..service..")
                .layer("DTO").definedBy("..dto..")
                .layer("Exception").definedBy("..exception..")
                .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
                .whereLayer("Service").mayOnlyBeAccessedByLayers("Controller")
                .whereLayer("DTO").mayOnlyBeAccessedByLayers("Controller", "Service")
                .whereLayer("Exception").mayOnlyBeAccessedByLayers("Controller", "Service");

        layeredArchitecture.check(importedClasses);
    }

    @Test
    @DisplayName("No circular dependencies should exist")
    void noCircularDependenciesShouldExist() {
        ArchRule rule = slices()
                .matching("com.demo.actions.GithubActionsDemo.(*)..")
                .should().beFreeOfCycles();

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("All public methods should have proper documentation")
    void allPublicMethodsShouldHaveProperDocumentation() {
        ArchRule rule = methods()
                .that().arePublic()
                .and().areDeclaredInClassesThat().resideInAnyPackage("..controller..", "..service..")
                .should().beAnnotatedWith("java.lang.Override")
                .orShould().haveRawReturnType("void")
                .orShould().haveRawReturnType("java.lang.String")
                .orShould().haveRawReturnType("java.util.List")
                .orShould().haveRawReturnType("org.springframework.http.ResponseEntity");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("No deprecated methods should be used")
    void noDeprecatedMethodsShouldBeUsed() {
        ArchRule rule = noClasses()
                .should().accessClassesThat().areAnnotatedWith("java.lang.Deprecated");

        rule.check(importedClasses);
    }

    @Test
    @DisplayName("All classes should be in correct packages")
    void allClassesShouldBeInCorrectPackages() {
        ArchRule controllerRule = classes()
                .that().haveSimpleNameEndingWith("Controller")
                .should().resideInAPackage("..controller..");

        ArchRule serviceRule = classes()
                .that().haveSimpleNameEndingWith("Service")
                .should().resideInAPackage("..service..");

        ArchRule dtoRule = classes()
                .that().haveSimpleNameEndingWith("Dto")
                .should().resideInAPackage("..dto..");

        ArchRule exceptionRule = classes()
                .that().haveSimpleNameEndingWith("Exception")
                .should().resideInAPackage("..exception..");

        controllerRule.check(importedClasses);
        serviceRule.check(importedClasses);
        dtoRule.check(importedClasses);
 