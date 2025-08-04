package architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.base.DescribedPredicate.alwaysTrue;
import static com.tngtech.archunit.core.domain.properties.HasName.AndFullName.Predicates.fullNameMatching;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@AnalyzeClasses(packages = "com.interview", importOptions = ImportOption.DoNotIncludeTests.class)
public class LayeredArchitectureTest
{
	@ArchTest
	static final ArchRule main_layer_dependencies_are_respected =
			layeredArchitecture().consideringAllDependencies()
		.layer("Access").definedBy("com.interview.resource..")
		.layer("Business").definedBy("com.interview.business..")
		.layer("Data").definedBy("com.interview.data..")
		.layer("Config").definedBy("com.interview.config..")

		.whereLayer("Access").mayNotBeAccessedByAnyLayer()
		.whereLayer("Business").mayOnlyBeAccessedByLayers("Access", "Business", "Config")
		.whereLayer("Data").mayOnlyBeAccessedByLayers("Access", "Business", "Config", "Data")
		.ignoreDependency(alwaysTrue(), fullNameMatching("com.interview.data.enumeration..*"));
}
