package reporting;

import reporting.ReportThemeFactory.Theme;

public class TestCase {

   public void runReport(){
//	   private Properties properties;
	   ReportTheme reportTheme = ReportThemeFactory.getReportsTheme(Theme.valueOf("CUSTOM"));

//	        // Create the ReportSettings and ReportTheme objects	
//	        ReportSettings reportSettings = new ReportSettings(System.getProperty("user.dir"),"Test");
	        ReportSettings	reportSettings = new ReportSettings(System.getProperty("user.dir"),"TestCaseSRM");
	        reportSettings.setDateFormatString("MMM-dd-yyyy hh:mm:ss a z");
			reportSettings.setLogLevel(4);
			reportSettings.setProjectName("SRM");
			reportSettings.setGenerateExcelReports(false);
			reportSettings.setGenerateHtmlReports(true);
			reportSettings.setGenerateSeeTestReports(false);
			reportSettings.setGeneratePerfectoReports(false);
			reportSettings
					.setTakeScreenshotFailedStep(true);
			reportSettings
					.setTakeScreenshotPassedStep(true);
			reportSettings.setConsolidateScreenshotsInWordDoc(false);
			reportSettings.setConsolidateScreenshotsInPDF(false);
			reportSettings.setLinkScreenshotsToTestLog(false);

	        // Instantiate the TestReporter class
			HtmlReport testReporter = new HtmlReport(reportSettings,reportTheme);
//	        testReporter.reportSettings = reportSettings;
//	        testReporter.reportTheme = reportTheme;

	        // Initialize the test log (create the HTML file)
	        testReporter.initializeTestLog();

	        // Start a test case report (this will create the file and add the initial header)
	        testReporter.startTestCaseReport("TestCase1");

	        // Simulate adding some steps to the report
	        testReporter.updateTestLog("1", "Step 1", "Test step 1 description", HtmlReport.Status.PASS, "screenshot1.png");
	        testReporter.updateTestLog("2", "Step 2", "Test step 2 description", HtmlReport.Status.FAIL, "screenshot2.png");
	        testReporter.updateTestLog("3", "Step 3", "Test step 3 description", HtmlReport.Status.PASS, "screenshot3.png");

	        // End the test report (close the table and HTML tags)
	        testReporter.endTestReport();

	        System.out.println("Test report generated successfully!");
	    
    }
}
