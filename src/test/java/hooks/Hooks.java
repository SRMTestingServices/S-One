package hooks;

import base.DriverManager;
import io.cucumber.java.*;
import org.openqa.selenium.WebDriver;
import reporting.HtmlReportGenerator;
import reporting.Status;
import reporting.TestStep;
import utils.ScreenshotUtil;
import utils.StepNamePlugin;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Hooks {
    private static final ThreadLocal<Map<String, Object>> testContext = ThreadLocal.withInitial(() -> {
        Map<String, Object> map = new ConcurrentHashMap<>();
        map.put("testSteps", new ArrayList<TestStep>());
        return map;
    });

    @Before(order = 1)
    public void setup(Scenario scenario) {
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
        long stepDuration = System.currentTimeMillis() - (long) testContext.get().get("stepStartTime");

        // Get step name from your existing plugin
        String stepName = StepNamePlugin.getCurrentStepName();
        if (stepName == null) {
            stepName = "Unknown Step";
        }

        // Capture screenshot only for failed steps (change to true for all steps)
        String screenshotPath = ScreenshotUtil.captureScreenshot(driver, "step_" + testSteps.size());

        testSteps.add(new TestStep(
                String.valueOf(testSteps.size() + 1),
                stepName.split(" ")[0], // Gets Given/When/Then
                stepName,
                scenario.isFailed() ? Status.FAIL : Status.PASS,
                screenshotPath,
                scenario.isFailed() ? getErrorMessage() : null,
                stepDuration
        ));
    }

    @After
    public void tearDown(Scenario scenario) {
        WebDriver driver = (WebDriver) testContext.get().get("driver");
        List<TestStep> testSteps = (List<TestStep>) testContext.get().get("testSteps");
        long scenarioDuration = System.currentTimeMillis() - (long) testContext.get().get("startTime");

        HtmlReportGenerator.generateHtmlReport(
                driver.getClass().getSimpleName(),
                scenario.getName(),
                "ModuleN Project",
                testSteps,
                !scenario.isFailed(),
                scenarioDuration
        );

        DriverManager.quitDriver();
        testContext.remove();
        StepNamePlugin.getCurrentStepName(); // Clears thread local
    }

    private String getErrorMessage() {
        Throwable error = StepNamePlugin.getCurrentStepError();
        if (error == null) return "Unknown error occurred";
        // Get root cause
        Throwable rootCause = error;
        while (rootCause.getCause() != null) {
            rootCause = rootCause.getCause();
        }
        return String.format("%s: %s",
                error.getClass().getSimpleName(),
                error.getMessage());
    }
}