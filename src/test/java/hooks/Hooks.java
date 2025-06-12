package hooks;

import base.DriverManager;
import io.cucumber.java.*;
import org.openqa.selenium.WebDriver;
import reporting.HtmlReportGenerator;
import reporting.ReportManager;
import reporting.Status;
import reporting.TestStep;
import utils.ConfigReader;
import utils.ScreenshotUtil;
import utils.StepNamePlugin;
import utils.TestContextUtil;
import java.util.List;
import java.util.Map;


public class Hooks {

    private static final String PROJECT_NAME = ConfigReader.getProperty("project.name");

    @Before(order = 1)
    public void setup(Scenario scenario) {
        // Initialize reporting structure once per test run
        if (ReportManager.getCurrentReportDirectory() == null) {
            HtmlReportGenerator.initializeReportFolder(PROJECT_NAME);
        }

        WebDriver driver = DriverManager.getDriver();
        TestContextUtil.getTestContext().put("driver", driver);
        TestContextUtil.getTestContext().put("scenario", scenario);
        TestContextUtil.getTestContext().put("startTime", System.currentTimeMillis());
    }

    @BeforeStep
    public void beforeStep() {
        TestContextUtil.getTestContext().put("stepStartTime", System.currentTimeMillis());
    }

    @AfterStep
    public void afterStep(Scenario scenario) {
        WebDriver driver = (WebDriver) TestContextUtil.getTestContext().get("driver");
        List<TestStep> testSteps = TestContextUtil.getCurrentTestSteps();
        int stepNumber = TestContextUtil.getStepCounter().get();
        long stepDuration = System.currentTimeMillis() - (long) TestContextUtil.getTestContext().get("startTime");

        String stepName = StepNamePlugin.getCurrentStepName();
        if (stepName == null) {
            stepName = "Unknown Step";
        }

        // Capture screenshot with consistent naming
        String screenshotPath = null;
        if (driver != null) {
            screenshotPath = ReportManager.getScreenshotPath("step_" + stepNumber);
            ScreenshotUtil.captureScreenshot(driver, screenshotPath);
        }
        String screenshotFullPath = ScreenshotUtil.captureScreenshot(driver, "step_" + stepNumber);
        testSteps.add(new TestStep(
                String.valueOf(stepNumber),
                stepName,
                scenario.isFailed() ? Status.FAIL : Status.PASS,
                screenshotFullPath,
                scenario.isFailed() ? getErrorMessage() : null,
                stepDuration
        ));

        TestContextUtil.getStepCounter().incrementAndGet();
    }

    @After
    public void tearDown(Scenario scenario) {
        WebDriver driver = (WebDriver) TestContextUtil.getTestContext().get("driver");
        List<TestStep> testSteps = TestContextUtil.getCurrentTestSteps();
        long scenarioDuration = System.currentTimeMillis() - (long) TestContextUtil.getTestContext().get("startTime");

        // Generate individual test report
        HtmlReportGenerator.generateHtmlReport(
                DriverManager.getBrowserName(),
                scenario.getName(),
                PROJECT_NAME,
                testSteps,
                !scenario.isFailed(),
                scenarioDuration
        );

        // Clean up
        if (driver != null) {
            DriverManager.quitDriver();
        }
        StepNamePlugin.clearCurrentStep(); // Clears thread local
    }

    private String getErrorMessage() {
        Throwable error = StepNamePlugin.getCurrentStepError();
        if (error == null) return "Unknown error occurred";

        Throwable rootCause = error;
        while (rootCause.getCause() != null) {
            rootCause = rootCause.getCause();
        }
        return String.format("%s: %s",
                error.getClass().getSimpleName(),
                error.getMessage());
    }
    public static Map<String, Object> getTestContext() {
        return TestContextUtil.getTestContext();
    }
}