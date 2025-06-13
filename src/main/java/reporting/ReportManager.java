package reporting;

import org.openqa.selenium.WebDriver;
import utils.ConfigReader;
import utils.ScreenshotUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ReportManager {
    // Thread-local storage for individual test steps
    private static final ThreadLocal<List<TestStep>> testSteps =
            ThreadLocal.withInitial(CopyOnWriteArrayList::new);
    private static final ThreadLocal<AtomicInteger> stepCounter =
            ThreadLocal.withInitial(() -> new AtomicInteger(1));

    // Track all test executions for the main report
    private static final Map<String, TestExecution> testExecutions = new ConcurrentHashMap<>();
    private static final AtomicLong totalExecutionStartTime = new AtomicLong(System.currentTimeMillis());

    // Report configuration
    private static final String projectName = ConfigReader.getProperty("project.name");
    private static String currentReportDirectory;

    public static void initializeReportDirectory() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        currentReportDirectory = "target/reports/" + projectName + "_" + dateFormat.format(new Date()) + "/";

        try {
            Files.createDirectories(Paths.get(currentReportDirectory + "test-reports/"));
            Files.createDirectories(Paths.get(currentReportDirectory + "test-reports/screenshots/"));
        } catch (IOException e) {
            System.err.println("Failed to create report directories: " + e.getMessage());
        }
    }

    public static String getCurrentReportDirectory() {
        if (currentReportDirectory == null) {
            initializeReportDirectory();
        }
        return currentReportDirectory;
    }

    public static String getScreenshotPath(String prefix) {
        return Paths.get(getCurrentReportDirectory())
                .resolve("test-reports")
                .resolve("screenshots")
                .resolve(System.currentTimeMillis() + ".png")
                .toString()
                .replace("\\", "/");
    }

    public static void addDynamicStep(WebDriver driver, String description,
                                      Status status, boolean takeScreenshot,
                                      String errorMessage, long executionTime) {
        List<TestStep> steps = testSteps.get();
        AtomicInteger counter = stepCounter.get();

        String screenshotPath = takeScreenshot ?
                ScreenshotUtil.captureFullPageScreenshot("step_" + counter.get()) :
                null;

        steps.add(new TestStep(
                String.valueOf(counter.getAndIncrement()),
                description,
                status,
                screenshotPath,
                errorMessage,
                executionTime
        ));
    }

    public static String getScreenshotDirectory() {
        return getCurrentReportDirectory() + "test-reports/screenshots/";
    }
    public static void registerTestExecution(String testName, String status,
                                             long duration, String reportPath) {
        testExecutions.put(testName, new TestExecution(
                testName,
                status,
                duration,
                reportPath
        ));
        generateMainReport();
    }

    public static List<TestStep> getTestSteps() {
        return testSteps.get();
    }

    public static void reset() {
        testSteps.remove();
        stepCounter.remove();
    }

    private static void generateMainReport() {
        try {
            String mainReportPath = getCurrentReportDirectory() + "main-report.html";
            String htmlContent = buildMainHtmlContent();
            Files.write(Paths.get(mainReportPath), htmlContent.getBytes());
        } catch (IOException e) {
            System.err.println("Failed to generate main report: " + e.getMessage());
        }
    }

    private static String buildMainHtmlContent() {
        StringBuilder html = new StringBuilder();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long totalDuration = System.currentTimeMillis() - totalExecutionStartTime.get();
        int passed = (int) testExecutions.values().stream()
                .filter(e -> e.status().equals("PASSED")).count();

        html.append("""
            <!DOCTYPE html>
            <html>
            <head>
                <title>Master Test Report</title>
                <style>
                    body { font-family: Arial, sans-serif; margin: 20px; }
                    table { border-collapse: collapse; width: 100%; }
                    th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
                    th { background-color: #f2f2f2; }
                    .passed { background-color: #d4edda; }
                    .failed { background-color: #f8d7da; }
                    .summary { margin-bottom: 20px; }
                    .timestamp { color: #666; font-size: 0.9em; }
                </style>
            </head>
            <body>
                <h1>Master Test Report -\s""").append(projectName).append("""
                </h1>
                <div class="summary">
                <p>Generated on: <span class="timestamp">""")
               .append(dateFormat.format(new Date())).append("</span></p>")
               .append("<p>Total Tests: ").append(testExecutions.size()).append("</p>")
               .append("<p>Passed: <span style='color:green'>").append(passed)
               .append("</span> | Failed: <span style='color:red'>")
               .append(testExecutions.size() - passed).append("</span></p>")
               .append("<p>Total Duration: ").append(formatDuration(totalDuration)).append("</p>")
               .append("</div><table><thead><tr>")
               .append("<th>Test Name</th><th>Status</th><th>Duration</th><th>Details</th></tr></thead><tbody>");

        testExecutions.values().stream()
                .sorted(Comparator.comparing(TestExecution::testName))
                .forEach(execution -> html.append("<tr class=\"").append(execution.status().toLowerCase()).append("\">")
                    .append("<td>").append(execution.testName()).append("</td>")
                    .append("<td>").append(execution.status()).append("</td>")
                    .append("<td>").append(formatDuration(execution.duration())).append("</td>")
                    .append("<td><a href=\"").append(execution.reportPath())
                    .append("\">View Details</a></td></tr>"));

        html.append("</tbody></table></body></html>");
        return html.toString();
    }

    private static String formatDuration(long millis) {
        return String.format("%02d:%02d:%02d",
                millis / 3600000,
                (millis % 3600000) / 60000,
                (millis % 60000) / 1000);
    }

    private record TestExecution(String testName, String status, long duration, String reportPath) {

    }
}