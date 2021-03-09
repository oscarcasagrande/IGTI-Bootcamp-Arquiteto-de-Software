package br.edu.fas;


import org.junit.Test;

import static org.junit.Assert.assertTrue;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;


public class FooArchitectTest {
    JavaClasses importedClasses = new ClassFileImporter().importPackages("br.edu.fas");

    @Test
    public void verificarArquitetura(){
        assertTrue(true);
    }

    @Test
    public void verificarDependenciasParaPersistencia() {
        
        ArchRule rule = classes().
        that().resideInAPackage("..persistence..")
        .should().onlyBeAccessed().byClassesThat().resideInAnyPackage("..persistence..", "..service..");
               
        ArchRule rule1 = classes()
        .that().resideInAPackage("..persistence..")
        .should().onlyHaveDependentClassesThat().resideInAnyPackage("..persistence..", "..service..");

        rule.check(importedClasses);

        rule1.check(importedClasses);
    }
}
