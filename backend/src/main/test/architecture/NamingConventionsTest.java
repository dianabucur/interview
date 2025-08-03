package architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(packages = "com.interview", importOptions = ImportOption.DoNotIncludeTests.class)
public class NamingConventionsTest
{
	@ArchTest
	static ArchRule business_objects_should_be_suffixed_and_annotated =
		classes().that().areTopLevelClasses().and()
			.resideInAPackage("..business")
			.should().haveSimpleNameEndingWith("Service")
			.andShould().beAnnotatedWith(Service.class);

	@ArchTest
	static ArchRule repositories_should_be_suffixed_and_annotated =
		classes().that().areTopLevelClasses().and()
			.resideInAPackage("..repository")
			.should().haveSimpleNameEndingWith("Repository")
			.andShould().beAnnotatedWith(Repository.class);

	@ArchTest
	static ArchRule controllers_should_be_suffixed_and_annotated =
		classes().that().areTopLevelClasses().and()
			.resideInAPackage("..resource")
			.should().haveSimpleNameEndingWith("Resource")
			.andShould().beAnnotatedWith(RestController.class);
}
