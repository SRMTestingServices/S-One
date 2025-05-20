package reporting;

import lombok.Getter;
import utils.StepNamePlugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HtmlReportGenerator {
	private static final String BASE_REPORT_DIR = "target/reports/";
	@Getter
	private static String reportFolderName;

	public static String getCurrentReportFolder() {
		return ReportManager.getCurrentReportDirectory();
	}

	public static void initializeReportFolder(String projectName) {
		String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		reportFolderName = projectName + "_" + timestamp;
		ReportManager.initializeReportDirectory(); // Let ReportManager handle directory creation
	}

	public static void generateHtmlReport(String browser, String testName,
										  String projectName, List<TestStep> testSteps,
										  boolean isTestPassed, long durationMs) {
		try {
			// Generate report filename
			String timestamp = new SimpleDateFormat("HHmmss").format(new Date());
			String filename = String.format("%stest-reports/%s_%s.html",
					ReportManager.getCurrentReportDirectory(),
					testName.replace(" ", "_"),
					timestamp);

			// Build HTML content with link to main report
			String htmlContent = buildHtmlContent(browser, testName, projectName,
					testSteps, isTestPassed, durationMs, filename);

			// Write to file
			Files.write(Paths.get(filename), htmlContent.getBytes());
			System.out.println("Report generated: " + filename);

			// Register with ReportManager
			ReportManager.registerTestExecution(
					testName,
					isTestPassed ? "PASSED" : "FAILED",
					durationMs,
					"test-reports/" + Paths.get(filename).getFileName().toString()
			);
		} catch (IOException e) {
			System.err.println("Failed to generate HTML report: " + e.getMessage());
		}
	}

	private static String buildHtmlContent(String browser, String testName,
										   String projectName, List<TestStep> testSteps,
										   boolean isTestPassed, long durationMs,
										   String reportPath) {
		StringBuilder html = new StringBuilder();
		int passedSteps = (int) testSteps.stream().filter(s -> s.getStatus() == Status.PASS).count();
		double passRate = testSteps.isEmpty() ? 0 : (passedSteps * 100.0 / testSteps.size());

		html.append("""
<!DOCTYPE html>
<html>
<head>
    <meta charset='UTF-8'>
    <title>Test Report -""").append(testName).append("""
    </title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; color: #333; }
        .report-header { text-align: center; margin-bottom: 20px; padding-bottom: 15px; border-bottom: 1px solid #eee; }
        .feature-name { font-size: 24px; font-weight: bold; margin-bottom: 10px; }
        .metadata { display: flex; justify-content: center; flex-wrap: wrap; gap: 15px; margin-bottom: 15px; font-size: 14px; }
        .metadata-item { display: flex; align-items: center; }
        .metadata-label { font-weight: bold; margin-right: 5px; }
        table { width: 100%; border-collapse: collapse; margin-bottom: 20px; }
        th, td { padding: 10px; text-align: left; border: 0.5px solid #0e0e0e; }
        th { background-color: #f5f5f5; font-weight: bold; }
        .status-pass { color: #4CAF50; font-weight: bold; }
        .status-fail { color: #f44336; font-weight: bold; }
        .summary { text-align: center; font-size: 14px; margin-top: 20px; }
        .screenshot { max-width: 200px; cursor: pointer; transition: transform 0.3s; }
        .main-report-link { margin-top: 20px; text-align: center; }
    </style>
</head>
<body>
    <div class="report-header">
        <div class="feature-name">""").append(testName).append("""
        </div>
        <div class="metadata">
            <div class="metadata-item">
                <span class="metadata-label">Project:</span>
                <span>""").append(projectName).append("""
                </span>
            </div>
            <div class="metadata-item">
                <span class="metadata-label">Browser:</span>
                <span>""").append(browser).append("""
                </span>
            </div>
            <div class="metadata-item">
                <span class="metadata-label">Status:</span>
                <span class=\"""").append(isTestPassed ? "status-pass" : "status-fail").append("\">")
				.append(isTestPassed ? "PASSED" : "FAILED").append("""
                </span>
            </div>
            <div class="metadata-item">
                <span class="metadata-label">Duration:</span>
                <span>""").append(formatDuration(durationMs)).append("""
                </span>
            </div>
            <div class="metadata-item">
                <span class="metadata-label">Pass Rate:</span>
                <span>""").append(String.format("%.1f%%", passRate)).append("""
                </span>
            </div>
        </div>
    </div>""");

		// Add link to main report
		html.append("""
            <div class="main-report-link">
                <a href="../main-report.html">← Back to Main Report</a>
            </div>
            <table>
                <thead>
                    <tr>
                        <th>Step #</th>
                        <th>Description</th>
                        <th>Status</th>
                        <th>Screenshot</th>
                        <th>Duration</th>
                    </tr>
                </thead>
                <tbody>""");

		// Add test steps
		for (TestStep step : testSteps) {
			html.append("<tr><td>").append(step.getStepNumber()).append("</td><td>");

			String stepDescription = step.getStepDescription();
			if (step.getStatus() == Status.FAIL && step.getErrorMessage() != null) {
				stepDescription += "<br><span style='color:red;'>Error: " +
						step.getErrorMessage() + "</span>";
			}

			html.append(stepDescription).append("</td><td class=\"")
					.append(step.getStatus() == Status.PASS ? "status-pass" : "status-fail").append("\">")
					.append(step.getStatus()).append("</td><td>")
					.append(getScreenshotHtml(step)).append("</td><td>")
					.append(step.getExecutionTime()).append(" ms</td></tr>");
		}

		html.append("</tbody></table><div class=\"summary\"><strong>Total Steps: ").append(testSteps.size())
				.append(" | </strong><strong style=\"color: #4CAF50\">Passed: ").append(passedSteps)
				.append("</strong><strong> | </strong><strong style=\"color: #f44336\">Failed: ")
				.append(testSteps.size() - passedSteps)
				.append("</strong></div></body></html>");

		return html.toString();
	}

	private static String getScreenshotHtml(TestStep step) {
		if (step.getScreenshot() == null) {
			return "N/A";
		}
		return String.format("<a href='../test-reports/%s' target='_blank'>" +
						"<img src='../test-reports/%s' class='screenshot'></a>",
				step.getScreenshot(), step.getScreenshot());
	}

	private static String formatDuration(long millis) {
		return String.format("%02d:%02d:%02d.%03d",
				TimeUnit.MILLISECONDS.toHours(millis),
				TimeUnit.MILLISECONDS.toMinutes(millis) % 60,
				TimeUnit.MILLISECONDS.toSeconds(millis) % 60,
				millis % 1000);
	}
}