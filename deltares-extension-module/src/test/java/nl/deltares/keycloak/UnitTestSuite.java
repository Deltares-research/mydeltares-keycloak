package nl.deltares.keycloak;

import com.googlecode.junittoolbox.SuiteClasses;
import com.googlecode.junittoolbox.WildcardPatternSuite;
import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;

@RunWith(WildcardPatternSuite.class)
@Categories.IncludeCategory(UnitTestCategory.class)
@SuiteClasses("**/*Test.class")
public class UnitTestSuite {
    //Placeholder class
}

