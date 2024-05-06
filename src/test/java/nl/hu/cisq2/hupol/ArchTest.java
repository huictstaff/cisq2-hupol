package nl.hu.cisq2.hupol;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(packages = "nl.hu.cisq2.hupol")
public class ArchTest {

    @com.tngtech.archunit.junit.ArchTest
    public static final ArchRule myRule = classes()
            .that().resideInAPackage("..application..")
            .should().onlyBeAccessed().byAnyPackage("..presentation..", "..application..");

}
