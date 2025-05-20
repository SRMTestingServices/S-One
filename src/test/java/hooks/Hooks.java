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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Hooks {
    private static final ThreadLocal<Map<String, Object>> testContext = ThreadLocal.withInitial(() -> {
        Map<String, Object> map = new ConcurrentHashMap<>();
        map.put("testSteps", new ArrayList<TestStep>());
        map.put("stepCounter", 1);
        return map;
    });

    private static final String PROJECT_NAME = ConfigReader.getProperty("project.name");

    @Before(order = 1)
    public void setup(Scenario scenario) {
        // Initialize reporting structure once per test run
        if (ReportManager.getCurrentReportDirectory() == null) {
            HtmlReportGenerator.initializeReportFolder(PROJECT_NAME);
        }

        WebDriver driver = DriverManager.getDriver();
        testContext.get().put("driver", driver);
        testContext.get().put("scenario", scenario);
        testContext.get().put("startTime", System.currentTimeMillis());
    }

    @BeforeStep
    public void beforeStep() {
        testContext.get().put("stepStartTime", System.currentTimeMillis());
    }

    @AfterStep
    public void afterStep(Scenario scenario) {
        WebDriver driver = (WebDriver) testContext.get().get("driver");
        List<TestStep> testSteps = (List<TestStep>) testContext.get().get("testSteps");
        int stepNumber = (int) testContext.get().get("stepCounter");
        long stepDuration = System.currentTimeMillis() - (long) testContext.get().get("stepStartTime");

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

        testContext.get().put("stepCounter", stepNumber + 1);
    }

    @After
    public void tearDown(Scenario scenario) {
        WebDriver driver = (WebDriver) testContext.get().get("driver");
        List<TestStep> testSteps = (List<TestStep>) testContext.get().get("testSteps");
        long scenarioDuration = System.currentTimeMillis() - (long) testContext.get().get("startTime");

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
        testContext.remove();
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
}