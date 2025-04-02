package reporting;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HtmlReportGenerator {

	public static void generateHtmlReport(String browser, String testCaseName, String projectName,
			List<TestStep> testSteps, boolean imageLink) {
		int totalSteps = testSteps.size();
		int passCount = (int) testSteps.stream().filter(step -> step.status == Status.PASS).count();
		int failCount = totalSteps - passCount;

		String htmlContent = String.format("""
				<!DOCTYPE html>
				<html>
				<head>
				    <meta charset="UTF-8">
				    <title>Test Report - %s</title>
				    <style>
				        body {
				            background: linear-gradient(to right, #4CAF50, #81C784); /* Gradient background */
				            font-family: Verdana, Geneva, sans-serif;
				            text-align: center;
				        }
				        table {
				            width: 80%%;
				            margin: 20px auto;
				            border-collapse: collapse;
				            background-color: white;
				            box-shadow: 0px 0px 10px #ccc;
				        }
				        th, td {
				            padding: 10px;
				            border: 1px solid #ddd;
				            text-align: left;
				        }
				        th {
				            background-color: #4CAF50;
				            color: white;
				        }
				        .pass {
				            background-color: #c6efce;
				            color: #006400;
				            font-weight: bold;
				        }
				        .fail {
				            background-color: #ffc7ce;
				            color: #d8000c;
				            font-weight: bold;
				        }
				        a {
				            text-decoration: none;
				            color: blue;
				        }
				        .footer {
				            font-weight: bold;
				            margin-top: 20px;
				        }
				    </style>
				</head>
				<body>
				    <h2>Test Report - %s</h2>
				    <h3>Project: %s | Browser: %s</h3>
				    <table>
				        <tr>
				            <th>Step Number</th>
				            <th>Step Name</th>
				            <th>Step Description</th>
				            <th>Status</th>
				            <th>Screenshot</th>
				        </tr>
				""", testCaseName, testCaseName, projectName, browser);

		// Add test steps dynamically
		for (TestStep step : testSteps) {
			htmlContent += addTestStep(step, imageLink);
		}

		// Footer Summary Section
		htmlContent += String.format("""
				    </table>
				    <div class="footer">
				        <p>Total Steps: %d | Passed: %d | Failed: %d</p>
				    </div>
				</body>
				</html>
				""", totalSteps, passCount, failCount);

		try (FileWriter writer = new FileWriter("TestReport.html")) {
			writer.write(htmlContent);
			System.out.println("Test report generated successfully: TestReport.html");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String addTestStep(TestStep step, boolean imageLink) {
		if (imageLink) {
			return String.format("""
					    <tr>
					        <td>%s</td>
					        <td>%s</td>
					        <td>%s</td>
					        <td class="%s">%s</td>
					        <td><a href="%s" target="_blank">View Screenshot</a></td>
					    </tr>
					""", step.stepNumber, step.stepName, step.stepDescription, step.status.name().toLowerCase(),
					step.status, step.screenshot);
		} else {
			return String.format("""
					    <tr>
					        <td>%s</td>
					        <td>%s</td>
					        <td>%s</td>
					        <td class="%s">%s</td>
					        <td><img src="%s" alt="Screenshot" width="150"></td>
					    </tr>
					""", step.stepNumber, step.stepName, step.stepDescription, step.status.name().toLowerCase(),
					step.status, step.screenshot);
		}
	}

	public static void main(String[] args) {
		// Test data
		List<TestStep> testSteps = new ArrayList<>();
		testSteps.add(new TestStep("1", "Step 1", "Test step 1 description", Status.PASS,
				System.getProperty("user.dir") + "\\src\\test\\resources\\screenshots\\image1.png"));
		testSteps.add(new TestStep("2", "Step 2", "Test step 2 description", Status.FAIL,
				System.getProperty("user.dir") + "\\src\\test\\resources\\screenshots\\image2.png"));
		testSteps.add(new TestStep("3", "Step 3", "Test step 3 description", Status.PASS,
				System.getProperty("user.dir") + "\\src\\test\\resources\\screenshots\\image3.png"));

		// Generate report
		generateHtmlReport("Chrome", "Login Test", "Project X", testSteps, false);
	}
}
