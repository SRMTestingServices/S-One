package reporting;

import org.openqa.selenium.WebDriver;
import utils.ScreenshotUtil;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import utils.TestContextUtil;

public class DynamicStepAdder {

    public static void addStep(WebDriver driver, String description,
                               Status status, boolean takeScreenshot,
                               String errorMessage, long executionTime) {

        Map<String, Object> context = TestContextUtil.getTestContext();
        List<TestStep> testSteps = TestContextUtil.getCurrentTestSteps();
        AtomicInteger stepCounter = TestContextUtil.getStepCounter();

        // 2. Capture screenshot if needed
        String screenshotPath = null;
        if (takeScreenshot && driver != null) {
            screenshotPath = ScreenshotUtil.captureScreenshot(driver, "step_" + stepCounter.get());
        }

        // 3. Create and add the step
        testSteps.add(new TestStep(
                String.valueOf(stepCounter.getAndIncrement()),
                description,
                status,
                screenshotPath,
                errorMessage,
                executionTime
        ));
    }

    // Overload for automatic duration calculation
    public static void addStep(WebDriver driver, String description,
                               Status status, boolean takeScreenshot,
                               String errorMessage) {
        long startTime = System.currentTimeMillis();
        addStep(driver, description, status, takeScreenshot,
                errorMessage, System.currentTimeMillis() - startTime);
    }
}