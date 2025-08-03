package architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = "com.interview", importOptions = ImportOption.DoNotIncludeTests.class)
public class DependencyRulesTest
{
	@ArchTest
	static final ArchRule repositories_and_utilities_should_not_be_used_in_access_classes =
		noClasses().that().resideInAPackage("..resource..")
			.should().dependOnClassesThat().resideInAPackage("..repository..")
			.orShould().dependOnClassesThat().resideInAPackage("..utility..");
}
