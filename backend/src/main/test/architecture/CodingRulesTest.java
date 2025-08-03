package architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.library.GeneralCodingRules.*;

@AnalyzeClasses(packages = "com.interview", importOptions = ImportOption.DoNotIncludeTests.class)
public class CodingRulesTest {
	@ArchTest
	static final ArchRule no_classes_should_throw_generic_exceptions = NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS;
	@ArchTest
	static final ArchRule no_classes_should_use_java_util_logging = NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;
	@ArchTest
	static final ArchRule no_classes_should_access_standard_streams = NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;
	
}
