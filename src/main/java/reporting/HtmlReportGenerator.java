package reporting;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HtmlReportGenerator {
	private static final String REPORT_DIR = "target/reports/";

	public static void generateHtmlReport(String browser, String testName,
										  String projectName, List<TestStep> testSteps,
										  boolean isTestPassed, long durationMs) {

		try {
			// Create report directory if it doesn't exist
			Files.createDirectories(Paths.get(REPORT_DIR));

			// Generate report filename with timestamp
			String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
			String filename = String.format("%sTestReport_%s_%s.html",
					REPORT_DIR,
					testName.replace(" ", "_"),
					timestamp);

			// Build HTML content
			String htmlContent = buildHtmlContent(browser, testName, projectName,
					testSteps, isTestPassed, durationMs);

			// Write to file
			Files.write(Paths.get(filename), htmlContent.getBytes());
			System.out.println("Report generated: " + filename);
		} catch (IOException e) {
			System.err.println("Failed to generate HTML report: " + e.getMessage());
		}
	}

	private static String buildHtmlContent(String browser, String testName,
										   String projectName, List<TestStep> testSteps,
										   boolean isTestPassed, long durationMs) {

		StringBuilder html = new StringBuilder();
		int passedSteps = (int) testSteps.stream().filter(s -> s.getStatus() == Status.PASS).count();
		double passRate = testSteps.isEmpty() ? 0 : (passedSteps * 100.0 / testSteps.size());

		// HTML Header and CSS
		html.append("<!DOCTYPE html>\n")
				.append("<html>\n<head>\n")
				.append("<meta charset='UTF-8'>\n")
				.append("<title>Test Report - ").append(testName).append("</title>\n")
				.append("<style>\n")
				.append("  body { font-family: Arial, sans-serif; margin: 20px; }\n")
				.append("  .header { background: #f5f5f5; padding: 15px; border-radius: 5px; margin-bottom: 20px; }\n")
				.append("  table { width: 100%; border-collapse: collapse; margin-bottom: 20px; }\n")
				.append("  th, td { padding: 10px; text-align: left; border-bottom: 1px solid #ddd; }\n")
				.append("  th { background-color: ").append(isTestPassed ? "#4CAF50" : "#f44336").append("; color: white; }\n")
				.append("  .pass { color: #4CAF50; }\n")
				.append("  .fail { color: #f44336; }\n")
				.append("  .screenshot { max-width: 200px; cursor: pointer; transition: transform 0.3s; }\n")
				.append("  .screenshot:hover { transform: scale(1.5); }\n")
				.append("  .error { color: #d32f2f; font-size: 0.9em; margin-top: 5px; }\n")
				.append("  .summary { font-weight: bold; margin-top: 20px; }\n")
				.append("</style>\n</head>\n<body>\n");

		// Report Header
		html.append("<div class='header'>\n")
				.append("<h2>").append(testName).append("</h2>\n")
				.append("<p><strong>Project:</strong> ").append(projectName).append("</p>\n")
				.append("<p><strong>Browser:</strong> ").append(browser).append("</p>\n")
				.append("<p><strong>Status:</strong> <span class='")
				.append(isTestPassed ? "pass'>PASSED" : "fail'>FAILED").append("</span></p>\n")
				.append("<p><strong>Duration:</strong> ").append(formatDuration(durationMs)).append("</p>\n")
				.append("<p><strong>Pass Rate:</strong> ").append(String.format("%.1f%%", passRate)).append("</p>\n")
				.append("</div>\n");

		// Test Steps Table
		html.append("<table>\n")
				.append("<tr>\n")
				.append("  <th>Step #</th>\n")
				.append("  <th>Description</th>\n")
				.append("  <th>Status</th>\n")
				.append("  <th>Screenshot</th>\n")
				.append("  <th>Duration</th>\n")
				.append("</tr>\n");

		// Add each test step
		for (TestStep step : testSteps) {
			html.append("<tr>\n")
					.append("  <td>").append(step.getStepNumber()).append("</td>\n")
					.append("  <td>").append(step.getStepDescription()).append("</td>\n")
					.append("  <td class='").append(step.getStatus().name().toLowerCase()).append("'>")
					.append(step.getStatus()).append("</td>\n")
					.append("  <td>").append(getScreenshotHtml(step.getScreenshot())).append("</td>\n")
					.append("  <td>").append(step.getExecutionTime()).append(" ms</td>\n")
					.append("</tr>\n");

			// Add error message if failed
			if (step.getStatus() == Status.FAIL && step.getErrorMessage() != null) {
				html.append("<tr>\n")
						.append("  <td colspan='6' class='error'>")
						.append(step.getErrorMessage()).append("</td>\n")
						.append("</tr>\n");
			}
		}

		// Footer
		html.append("</table>\n")
				.append("<div class='summary'>\n")
				.append("<p>Total Steps: ").append(testSteps.size())
				.append(" | Passed: ").append(passedSteps)
				.append(" | Failed: ").append(testSteps.size() - passedSteps).append("</p>\n")
				.append("</div>\n")
				.append("<script>\n")
				.append("document.querySelectorAll('.screenshot').forEach(img => {\n")
				.append("  img.addEventListener('click', () => window.open(img.src, '_blank'));\n")
				.append("});\n")
				.append("</script>\n")
				.append("</body>\n</html>");

		return html.toString();
	}

	private static String getScreenshotHtml(String screenshotPath) {
		if (screenshotPath == null || screenshotPath.isEmpty()) {
			return "N/A";
		}

		// Normalize all path separators
		String normalizedPath = screenshotPath.replace("\\", "/");

		// Remove any file:/// prefix
		normalizedPath = normalizedPath.replace("file:///", "");

		// Handle absolute paths
		if (normalizedPath.startsWith("D:/Automation/S-One/")) {
			normalizedPath = normalizedPath.substring("D:/Automation/S-One/".length());
		}

		// Fix duplicate target/reports issue
		normalizedPath = normalizedPath.replace("target/reports/target/", "target/");

		// Ensure we only have the direct screenshots path
		if (normalizedPath.contains("screenshots/")) {
			normalizedPath = "screenshots/" + normalizedPath.split("screenshots/")[1];
		}

		// Create proper HTML with correct paths for both src and click action
		return String.format(
				"<a href='%s' target='_blank'>" +
						"<img src='%s' class='screenshot'>" +
						"</a>",
				normalizedPath,
				normalizedPath
		);
	}

	private static String formatDuration(long millis) {
		return String.format("%02d:%02d:%02d.%03d",
				TimeUnit.MILLISECONDS.toHours(millis),
				TimeUnit.MILLISECONDS.toMinutes(millis) % 60,
				TimeUnit.MILLISECONDS.toSeconds(millis) % 60,
				millis % 1000);
	}
}