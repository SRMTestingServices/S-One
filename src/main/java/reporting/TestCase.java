package reporting;

public class TestCase {

   public static void main(String[] args) {

//	        // Create the ReportSettings and ReportTheme objects
	        ReportSettings reportSettings = new ReportSettings(System.getProperty("user.dir"),"Test");
//	        reportSettings.setWidth ("500px");
//	        reportSettings.setHeight("300px");

	        ReportTheme reportTheme = new ReportTheme();
	        reportTheme.setHeadingBackColor("blue");
	        reportTheme.setHeadingForeColor("white");
	        reportTheme.setSubHeadingBackColor("lightgray");
	        reportTheme.setSubHeadingForeColor("black");
	        reportTheme.setSubSectionBackColor("white");
	        reportTheme.setContentBackColor("black");

	        // Instantiate the TestReporter class
	        TestReporter testReporter = new TestReporter(reportSettings,reportTheme);
//	        testReporter.reportSettings = reportSettings;
//	        testReporter.reportTheme = reportTheme;

	        // Initialize the test log (create the HTML file)
	        testReporter.initializeTestLog();

	        // Start a test case report (this will create the file and add the initial header)
	        testReporter.startTestCaseReport("TestCase1");

	        // Simulate adding some steps to the report
	        testReporter.updateTestLog("1", "Step 1", "Test step 1 description", TestReporter.Status.PASS, "screenshot1.png");
	        testReporter.updateTestLog("2", "Step 2", "Test step 2 description", TestReporter.Status.FAIL, "screenshot2.png");
	        testReporter.updateTestLog("3", "Step 3", "Test step 3 description", TestReporter.Status.PASS, "screenshot3.png");

	        // End the test report (close the table and HTML tags)
	        testReporter.endTestReport();

	        System.out.println("Test report generated successfully!");
	    
    }
}
