package reporting;

import org.openqa.selenium.WebDriver;
import utils.ScreenshotUtil;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.CopyOnWriteArrayList;

public class ReportManager {
    private static final ThreadLocal<List<TestStep>> testSteps =
            ThreadLocal.withInitial(CopyOnWriteArrayList::new);
    private static final ThreadLocal<AtomicInteger> stepCounter =
            ThreadLocal.withInitial(() -> new AtomicInteger(1));

    public static void addDynamicStep(WebDriver driver, String description,
                                      Status status, boolean takeScreenshot,
                                      String errorMessage, long executionTime) {
        List<TestStep> steps = testSteps.get();
        AtomicInteger counter = stepCounter.get();

        String screenshotPath = takeScreenshot ?
                ScreenshotUtil.captureScreenshot(driver, "step_" + counter.get()) :
                null;

        steps.add(new TestStep(
                String.valueOf(counter.getAndIncrement()),
                description,
                status,
                screenshotPath,
                errorMessage,
                executionTime // You'll need to track actual duration if needed
        ));
    }

    public static List<TestStep> getTestSteps() {
        return testSteps.get();
    }

    public static void reset() {
        testSteps.remove();
        stepCounter.remove();
    }
}