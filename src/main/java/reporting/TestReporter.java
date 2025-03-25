package reporting;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;


public class TestReporter {

    private String testLogPath;
    private String resultSummaryPath= System.getProperty("user.dir")+ File.separator+"Summary" + ".html";
	private ReportSettings reportSettings;
	private ReportTheme reportTheme;
	
	public TestReporter(ReportSettings reportSettings, ReportTheme reportTheme) {
		this.reportSettings = reportSettings;
		this.reportTheme = reportTheme;
	}

    private String getThemeCss() {
        return "\t\t <style type='text/css'> \n" +
                "\t\t\t body { \n" +
                "\t\t\t\t background-color: CUSTOM; \n" + // CUSTOM should be replaced with a valid color or variable if needed.
                "\t\t\t\t font-family: Verdana, Geneva, sans-serif; \n" +
                "\t\t\t\t text-align: center; \n" +
                "\t\t\t } \n\n" +

                "\t\t\t small { \n" +
                "\t\t\t\t font-size: 0.7em; \n" +
                "\t\t\t } \n\n" +

                "\t\t\t table { \n" +
                "\t\t\t\t width: 95%; \n" +
                "\t\t\t\t margin-left: auto; \n" +
                "\t\t\t\t margin-right: auto; \n" +
                "\t\t\t } \n\n" +

                "\t\t\t tr.heading { \n" +
                "\t\t\t\t background-color: " + reportTheme.getHeadingBackColor() + "; \n" +
                "\t\t\t\t color: " + reportTheme.getHeadingForeColor() + "; \n" +
                "\t\t\t\t font-size: 0.6em; \n" +
                "\t\t\t\t font-weight: bold; \n" +
                "\t\t\t } \n\n" +

                "\t\t\t tr.subheading { \n" +
                "\t\t\t\t background-color: " + reportTheme.getsubHeadingBackColor() + "; \n" +
                "\t\t\t\t color: " + reportTheme.getsubHeadingForeColor() + "; \n" +
                "\t\t\t\t font-weight: bold; \n" +
                "\t\t\t\t font-size: 0.6em; \n" +
                "\t\t\t\t text-align: justify; \n" +
                "\t\t\t } \n\n" +

                "\t\t\t tr.content { \n" +
                "\t\t\t\t background-color: " + reportTheme.getsubSectionBackColor() + "; \n" +
                "\t\t\t\t color: " + reportTheme.getContentBackColor() + "; \n" +
                "\t\t\t\t font-size: 0.8em; \n" +
                "\t\t\t } \n\n" +

                "\t\t\t td { \n" + 
                "\t\t\t\t padding: 8px; \n" + 
                "\t\t\t\t text-align: inherit; \n" + // Removed the extra '\\0/' syntax issue.
                "\t\t\t\t word-wrap: break-word; \n" + 
                "\t\t\t\t max-width: 450px; \n" + 
                "\t\t\t } \n\n" +

                "\t\t\t th { \n" + 
                "\t\t\t\t padding: 8px; \n" + 
                "\t\t\t\t text-align: inherit; \n" + // Removed the extra '\\0/' syntax issue.
                "\t\t\t\t word-break: break-all; \n" + 
                "\t\t\t\t max-width: 450px; \n" + 
                "\t\t\t } \n\n" +

                "\t\t\t td.justified { \n" + 
                "\t\t\t\t text-align: justify; \n" + 
                "\t\t\t } \n\n" +

                "\t\t\t td.pass { \n" + 
                "\t\t\t\t font-weight: bold; \n" + 
                "\t\t\t\t color: green; \n" + 
                "\t\t\t } \n\n" +

                "\t\t\t td.fail { \n" + 
                "\t\t\t\t font-weight: bold; \n" + 
                "\t\t\t\t color: red; \n" + 
                "\t\t\t } \n\n" + 

                "\t\t\t td.done, td.screenshot { \n" + 
                "\t\t\t\t font-weight: bold; \n" + 
                "\t\t\t\t color: black; \n" + 
                "\t\t\t } \n\n" +

                "\t\t\t td.debug { \n" + 
                "\t\t\t\t font-weight: bold; \n" + 
                "\t\t\t\t color: blue; \n" + 
                "\t\t\t } \n\n" +

                "\t\t\t td.warning { \n" + 
                "\t\t\t\t font-weight: bold; \n" + 
                "\t\t\t\t color: orange; \n" + 
                "\t\t\t } \n" + 
                "\t\t\t img { \n" + 
                "\t\t\t\t width:" + reportSettings.getWidth() + "; \n" + 
                "\t\t\t\t height:" + reportSettings.getHeight() + "; \n" + 
                "\t\t\t } \n" + 
                "\t\t </style> \n\n";
    }

    private String getJavascriptFunctions() {
        return "\t\t <script> \n" + "\t\t\t function toggleMenu(objID) { \n"
                + "\t\t\t\t var ob = document.getElementById(objID).style; \n"
                + "\t\t\t\t if(ob.display === 'none') { \n" + "\t\t\t\t\t ob.display='block'; \n" + "\t\t\t\t } \n"
                + "\t\t\t\t else { \n" + "\t\t\t\t\t ob.display='none'; \n" + "\t\t\t\t } \n" + "\t\t\t } \n"
                + "\t\t </script> \n";
    }

    /* TEST LOG FUNCTIONS */

    public void initializeTestLog() {
        File testLogFile = new File(resultSummaryPath);
        try {
            testLogFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while creating HTML test log file");
        }

        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(testLogFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot find HTML test log file");
        }
        PrintStream printStream = new PrintStream(outputStream);

        String testLogHeadSection;
        testLogHeadSection = "<!DOCTYPE html> \n" + "<html> \n" + "\t <head> \n" + "\t\t <meta charset='UTF-8'> \n"
                + "\t\t <title> SRM- University "
                + " Automation Execution Results" + "</title> \n\n" + getThemeCss() + getJavascriptFunctions()
                + "\t </head> \n";

        printStream.println(testLogHeadSection);
        printStream.close();
    }

    // Method to start the report generation for a test case
    public void startTestCaseReport(String testCaseName) {
        // Generate the filename based on the test case name
        testLogPath = testCaseName + "_test_report.html"; // File will be saved as <TestCaseName>_test_report.html

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(testLogPath, false)); // false to
                                                                                                    // overwrite each
                                                                                                    // time a test
                                                                                                    // starts
            bufferedWriter.write("<html>\n<head>\n<title>Test Report - " + testCaseName + "</title>\n");
            bufferedWriter.write("<link rel='stylesheet' type='text/css' href='test-report.css'>\n"); // External CSS
            bufferedWriter.write("</head>\n<body>\n");
            bufferedWriter.write("<h1>Test Report - " + testCaseName + "</h1>\n");
            bufferedWriter.write("<table>\n");
            bufferedWriter.write(
                    "<thead><tr><th>Step Number</th><th>Step Name</th><th>Step Description</th><th>Status</th><th>Screenshot</th></tr></thead>\n");

            bufferedWriter.close(); // Close for now; will append during test execution
        } catch (IOException e) {
            e.printStackTrace();
            throw new FrameworkException("Error while starting HTML test log");
        }
    }

    // Method to log each step with details
    public void updateTestLog(String stepNumber, String stepName, String stepDescription, Status stepStatus,
                              String screenShotName) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(testLogPath, true));

            String stepStatusColor = stepStatus == Status.PASS ? "green" : "red"; // Green for pass, red for fail
            String stepRow = "<tr><td>" + stepNumber + "</td><td>" + stepName + "</td><td>" + stepDescription
                    + "</td><td style='color:" + stepStatusColor + ";'>" + stepStatus + "</td><td><img src='"
                    + screenShotName + "' alt='Step Screenshot' width='100'></td></tr>\n";

            bufferedWriter.write(stepRow);
            bufferedWriter.close(); // Close to ensure step is written and file is saved
        } catch (IOException e) {
            e.printStackTrace();
            throw new FrameworkException("Error while writing to HTML test log");
        }
    }

    // Method to complete the report after all steps are logged
    public void endTestReport() {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(testLogPath, true));
            bufferedWriter.write("</table>\n");
            bufferedWriter.write("</body>\n</html>\n");
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new FrameworkException("Error while ending the HTML test log");
        }
    }

    public enum Status {
        PASS,
        FAIL,
        SKIPPED
    }

    public class FrameworkException extends RuntimeException {
        public FrameworkException(String message) {
            super(message);
        }
    }
}
