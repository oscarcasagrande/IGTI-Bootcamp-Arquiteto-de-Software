package br.edu.fas;


import org.junit.Test;

import br.edu.fas.persistence.Dao;

import static org.junit.Assert.assertTrue;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.Architectures.LayeredArchitecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

public class FooArchitectTest {
    JavaClasses importedClasses = new ClassFileImporter().importPackages("br.edu.fas");

    @Test
    public void verificarArquitetura(){
        assertTrue(true);
    }

    @Test
    public void verificarDependenciasParaCamadaPersistencia() {
        ArchRule rule = classes()
            .that().resideInAPackage("..persistence..")
            .should().onlyHaveDependentClassesThat().resideInAnyPackage("..persistence..", "..service..");

        rule.check(importedClasses);
    }

    @Test
    public void verificarDependenciasDaCamadaPersistencia(){
        ArchRule rule = classes()
            .that().resideInAPackage("..persistence..")
            .should().onlyBeAccessed().byClassesThat().resideInAnyPackage("..service..");
               
        rule.check(importedClasses);
    }

    @Test
    public void verificarNomesDeClassesDaCamadaPersistencia(){
        ArchRule rule = classes()
            .that().haveSimpleNameEndingWith("Dao")
            .should().resideInAnyPackage("..persistence..");
               
        rule.check(importedClasses);
    }



    @Test
    public void verificarImplementacaoInterfaceDao(){
        ArchRule rule = classes()
            .that().implement(Dao.class)
            .should().resideInAnyPackage("..persistence..");
               
        rule.check(importedClasses);
    }

    @Test
    public void VerificarDependeciaCiclica(){
        ArchRule rule = slices()
        .matching("br.edu.fas.(*)..").should().beFreeOfCycles();

        rule.check(importedClasses);
    }

    @Test
    public void VerificarViolacaoDeCamadas(){
        ArchRule rule = layeredArchitecture()
        .layer("Service").definedBy("..service..")
        .layer("Persistence").definedBy("..persistence..")
        .whereLayer("Persistence").mayOnlyBeAccessedByLayers("Service");
        
        rule.check(importedClasses);
    }

}