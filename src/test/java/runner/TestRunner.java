package runner;

import com.aventstack.chaintest.plugins.ChainTestListener;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.Listeners;

@CucumberOptions(
        features = "src/test/resources/features",  // Path to feature files
        glue = {"steps", "hooks"},
        plugin = {"pretty", "html:reports/cucumber.html","utils.StepNamePlugin"},
        tags = "@TSCID098765",
        monochrome = true
)
@Listeners(ChainTestListener.class)
public class TestRunner extends AbstractTestNGCucumberTests {
}
