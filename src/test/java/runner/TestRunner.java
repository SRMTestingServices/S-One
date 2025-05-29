package runner;

import com.aventstack.chaintest.plugins.ChainTestListener;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import utils.ConfigReader;

@CucumberOptions(
        features = "classpath:features",  // Path to feature files
        glue = {"steps", "hooks"},
        plugin = {"pretty",
                "html:reports/cucumber.html",
                "json:reports/cucumber.json",
                "utils.StepNamePlugin",
                "timeline:reports/timeline"},
        tags = "@ApiTest",
        monochrome = true
)
@Listeners(ChainTestListener.class)
public class TestRunner extends AbstractTestNGCucumberTests {
    @BeforeSuite
    public void beforeSuite() {
        // Set thread count from config.properties
        System.setProperty("dataproviderthreadcount",
                String.valueOf(ConfigReader.getIntProperty("parallel.thread.count")));
    }

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
