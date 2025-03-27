package reporting;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;


public class HtmlReport {

    private String testLogPath;
    private String resultSummaryPath= System.getProperty("user.dir")+ File.separator+"Summary" + ".html";
	private ReportSettings reportSettings;
	private ReportTheme reportTheme;
	private String currentSection = "";
	private String currentSubSection = "";
	private int currentContentNumber = 1;
	
	public HtmlReport(ReportSettings reportSettings, ReportTheme reportTheme) {
		this.reportSettings = reportSettings;
		this.reportTheme = reportTheme;
	}

    private String getThemeCss() {
    	return "\t\t <style type='text/css'> \n" + "\t\t\t body { \n" + "\t\t\t\t background-color: "
				+ reportTheme.getContentForeColor() + "; \n" + "\t\t\t\t font-family: Verdana, Geneva, sans-serif; \n"
				+ "\t\t\t\t text-align: center; \n" + "\t\t\t } \n\n" +

				"\t\t\t small { \n" + "\t\t\t\t font-size: 0.7em; \n" + "\t\t\t } \n\n" +

				"\t\t\t table { \n" +
				// "\t\t\t\t border: 1px solid #4D7C7B; \n" +
				// "\t\t\t\t border-collapse: collapse; \n" +
				// "\t\t\t\t border-spacing: 0px; \n" +
				"\t\t\t\t width: 100%; \n" + "\t\t\t\t margin-left: auto; \n" + "\t\t\t\t margin-right: auto; \n"
				+ "\t\t\t } \n\n" +

				"\t\t\t tr.heading { \n" + "\t\t\t\t background-color: " + reportTheme.getHeadingBackColor() + "; \n"
				+ "\t\t\t\t color: " + reportTheme.getHeadingForeColor() + "; \n" + "\t\t\t\t font-size: 0.6em; \n"
				+ "\t\t\t\t font-weight: bold; \n" +

				"\t\t\t } \n\n" +

				"\t\t\t tr.subheading { \n" + "\t\t\t\t background-color: " + reportTheme.getsubHeadingBackColor()
				+ "; \n" + "\t\t\t\t color: " + reportTheme.getsubHeadingForeColor() + "; \n"
				+ "\t\t\t\t font-weight: bold; \n" + "\t\t\t\t font-size: 0.6em; \n"
				+ "\t\t\t\t text-align: justify; \n" + "\t\t\t } \n\n" +

				"\t\t\t tr.section { \n" + "\t\t\t\t background-color: " + reportTheme.getSectionBackColor() + "; \n"
				+ "\t\t\t\t color: " + reportTheme.getSectionForeColor() + "; \n" + "\t\t\t\t cursor: pointer; \n"
				+ "\t\t\t\t font-weight: bold; \n" + "\t\t\t\t font-size: 0.6em; \n"
				+ "\t\t\t\t text-align: justify; \n" + "\t\t\t } \n\n" +

				"\t\t\t tr.subsection { \n" + "\t\t\t\t background-color: " + reportTheme.getsubSectionBackColor()
				+ "; \n" + "\t\t\t\t cursor: pointer; \n" + "\t\t\t } \n\n" +

				"\t\t\t tr.content { \n" + "\t\t\t\t background-color: " + reportTheme.getsubSectionBackColor() + "; \n"
				+ "\t\t\t\t color: " + reportTheme.getContentBackColor() + "; \n" + "\t\t\t\t font-size: 0.8em; \n"
				+ "\t\t\t\t display: table-row; \n" + "\t\t\t } \n\n" +

				"\t\t\t td { \n" + "\t\t\t\t padding: 8px; \n" + "\t\t\t\t text-align: inherit\\0/; \n"
				+ "\t\t\t\t word-wrap: break-word; \n" + "\t\t\t\t max-width: 450px; \n" + "\t\t\t } \n\n" +

				"\t\t\t th { \n" + "\t\t\t\t padding: 8px; \n" + "\t\t\t\t text-align: inherit\\0/; \n"
				+ "\t\t\t\t word-break: break-all; \n" + "\t\t\t\t max-width: 450px; \n" + "\t\t\t } \n\n" +

				"\t\t\t td.justified { \n" + "\t\t\t\t text-align: justify; \n" + "\t\t\t } \n\n" +

				"\t\t\t td.pass { \n" + "\t\t\t\t font-weight: bold; \n" + "\t\t\t\t color: green; \n" + "\t\t\t } \n\n"
				+

				"\t\t\t td.fail { \n" + "\t\t\t\t font-weight: bold; \n" + "\t\t\t\t color: red; \n" + "\t\t\t } \n\n" +

				"\t\t\t td.done, td.screenshot { \n" + "\t\t\t\t font-weight: bold; \n" + "\t\t\t\t color: black; \n"
				+ "\t\t\t } \n\n" +

				"\t\t\t td.debug { \n" + "\t\t\t\t font-weight: bold; \n" + "\t\t\t\t color: blue; \n" + "\t\t\t } \n\n"
				+

				"\t\t\t td.warning { \n" + "\t\t\t\t font-weight: bold; \n" + "\t\t\t\t color: orange; \n"
				+ "\t\t\t } \n" + "\t\t\t img { \n" + "\t\t\t\t width:" + reportSettings.getWidth() + "; \n"
				+ "\t\t\t\t height:" + reportSettings.getHeight() + "; \n" + "\t\t\t } \n" + "\t\t </style> \n\n";
    }

    private String getJavascriptFunctions() {
        return "\t\t <script> \n" + "\t\t\t function toggleMenu(objID) { \n"
				+ "\t\t\t\t if (!document.getElementById) return; \n"
				+ "\t\t\t\t var ob = document.getElementById(objID).style; \n"
				+ "\t\t\t\t if(ob.display === 'none') { \n" + "\t\t\t\t\t try { \n"
				+ "\t\t\t\t\t\t ob.display='table-row-group'; \n" + "\t\t\t\t\t } catch(ex) { \n"
				+ "\t\t\t\t\t\t ob.display='block'; \n" + "\t\t\t\t\t } \n" + "\t\t\t\t } \n" + "\t\t\t\t else { \n"
				+ "\t\t\t\t\t ob.display='none'; \n" + "\t\t\t\t } \n" + "\t\t\t } \n" +

				"\t\t\t function toggleSubMenu(objId) { \n" + "\t\t\t\t for(i=1; i<10000; i++) { \n"
				+ "\t\t\t\t\t var ob = document.getElementById(objId.concat(i)); \n" + "\t\t\t\t\t if(ob === null) { \n"
				+ "\t\t\t\t\t\t break; \n" + "\t\t\t\t\t } \n" + "\t\t\t\t\t if(ob.style.display === 'none') { \n"
				+ "\t\t\t\t\t\t try { \n" + "\t\t\t\t\t\t\t ob.style.display='table-row'; \n"
				+ "\t\t\t\t\t\t } catch(ex) { \n" + "\t\t\t\t\t\t\t ob.style.display='block'; \n" + "\t\t\t\t\t\t } \n"
				+ "\t\t\t\t\t } \n" + "\t\t\t\t\t else { \n" + "\t\t\t\t\t\t ob.style.display='none'; \n"
				+ "\t\t\t\t\t } \n" + "\t\t\t\t } \n" + "\t\t\t } \n" + "\t\t </script> \n";
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
				+ "\t\t <title>" + reportSettings.getProjectName() + " - " + reportSettings.getReportName()
				+ " Automation Execution Results" + "</title> \n\n" + getThemeCss() + getJavascriptFunctions()
				+ "\t </head> \n";

        printStream.println(testLogHeadSection);
        printStream.close();
    }

	public void addTestLogHeading(String heading) {
//		if (!isTestLogHeaderTableCreated) {
//			createTestLogHeaderTable();
//			isTestLogHeaderTableCreated = true;
//		}
		createTestLogHeaderTable();

		BufferedWriter bufferedWriter;
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(testLogPath, true));

			String testLogHeading = "\t\t\t\t <tr class='heading'> \n"
					+ "\t\t\t\t\t <th colspan='4' style='font-family:Copperplate Gothic; font-size:1.4em;'> \n"
					+ "\t\t\t\t\t\t " + heading + " \n" + "\t\t\t\t\t </th> \n" + "\t\t\t\t </tr> \n";
			bufferedWriter.write(testLogHeading);
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException("Error while adding heading to HTML test log");
		}
	}

	private void createTestLogHeaderTable() {
		BufferedWriter bufferedWriter;
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(testLogPath, true));

			String testLogHeaderTable = "\t <body> \n" + "\t\t <table id='header'> \n" + "\t\t\t <thead> \n";
			bufferedWriter.write(testLogHeaderTable);
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException("Error while adding header table to HTML test log");
		}
	}

	public void addTestLogSubHeading(String subHeading1, String subHeading2, String subHeading3, String subHeading4) {
		BufferedWriter bufferedWriter;
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(testLogPath, true));

			String testLogSubHeading = "\t\t\t\t <tr class='subheading'> \n" + "\t\t\t\t\t <th>&nbsp;"
					+ subHeading1.replace(" ", "&nbsp;") + "</th> \n" + "\t\t\t\t\t <th>&nbsp;"
					+ subHeading2.replace(" ", "&nbsp;") + "</th> \n" + "\t\t\t\t\t <th>&nbsp;"
					+ subHeading3.replace(" ", "&nbsp;") + "</th> \n" + "\t\t\t\t\t <th>&nbsp;"
					+ subHeading4.replace(" ", "&nbsp;") + "</th> \n" + "\t\t\t\t </tr> \n";
			bufferedWriter.write(testLogSubHeading);
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException("Error while adding sub-heading to HTML test log");
		}
	}

	private void createTestLogMainTable() {
		BufferedWriter bufferedWriter;
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(testLogPath, true));

			String testLogMainTable = "\t\t\t </thead> \n" + "\t\t </table> \n\n" +

					"\t\t <table id='main'> \n";

			bufferedWriter.write(testLogMainTable);
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException("Error while adding main table to HTML test log");
		}
	}

	public void addTestLogTableHeadings() {
//		if (!isTestLogMainTableCreated) {
//		
//			isTestLogMainTableCreated = true;
//		}
		createTestLogMainTable();
		BufferedWriter bufferedWriter;
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(testLogPath, true));

			String testLogTableHeading = "\t\t\t <thead> \n" + "\t\t\t\t <tr class='heading'> \n"
					+ "\t\t\t\t\t <th>Step No</th> \n" + "\t\t\t\t\t <th>Step Name</th> \n"
					+ "\t\t\t\t\t <th>Description</th> \n" + "\t\t\t\t\t <th>Status</th> \n"
					+ "\t\t\t\t\t <th>Step Time</th> \n" + "\t\t\t\t\t <th>Screenshot</th> \n" + "\t\t\t\t </tr> \n"
					+ "\t\t\t </thead> \n\n";
			bufferedWriter.write(testLogTableHeading);
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException("Error while adding main table headings to HTML test log");
		}
	}

	public void addTestLogSection(String section) {
		String testLogSection = "";
		if (!"".equals(currentSection)) {
			testLogSection = "\t\t\t </tbody>";
		}

		currentSection = section.replaceAll("[^a-zA-Z0-9]", "");

		BufferedWriter bufferedWriter;
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(testLogPath, true));
//			if (globalprop.getProperty("MethodInclude").equals("True")) {
				testLogSection += "\t\t\t <tbody> \n" + "\t\t\t\t <tr class='section'> \n"
						+ "\t\t\t\t\t <td colspan='6' onclick=\"toggleMenu('" + currentSection + "')\">+ " + section
						+ "</td> \n" + "\t\t\t\t </tr> \n" + "\t\t\t </tbody> \n" + "\t\t\t <tbody id='"
						+ currentSection + "' style='display:table-row-group'> \n";
				bufferedWriter.write(testLogSection);
				bufferedWriter.close();
//			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException("Error while adding section to HTML test log");
		}
	}

	public void addTestLogSubSection(String subSection) {
		currentSubSection = subSection.replaceAll("[^a-zA-Z0-9]", "");
		currentContentNumber = 1;

		BufferedWriter bufferedWriter;
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(testLogPath, true));
//			if (globalprop.getProperty("MethodInclude").equals("True")) {
				String testLogSubSection = "\t\t\t\t <tr class='subheading subsection'> \n"
						+ "\t\t\t\t\t <td colspan='6' onclick=\"toggleSubMenu('" + currentSection + currentSubSection
						+ "')\">&nbsp;+ " + subSection + "</td> \n" + "\t\t\t\t </tr> \n";
				bufferedWriter.write(testLogSubSection);
				bufferedWriter.close();
//			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException("Error while adding sub-section to HTML test log");
		}
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
			//stepName = StringEscapeUtils.escapeHtml4(stepName);
			//stepDescription = StringEscapeUtils.escapeHtml4(stepDescription);
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(testLogPath, true));

			String testStepRow = "\t\t\t\t <tr class='content' id='" + currentSection + currentSubSection
					+ currentContentNumber + "'> \n" + "\t\t\t\t\t <td class='justified'>" + stepNumber + "</td> \n"
					+ "\t\t\t\t\t <td class='justified'>" + stepName + "</td> \n";
			currentContentNumber++;

			switch (stepStatus) {
			case FAIL:
				if (reportSettings.shouldTakeScreenshotFailedStep()) {
					if (screenShotName.toLowerCase().endsWith(".png") || screenShotName.toLowerCase().endsWith(".jpg")
							|| screenShotName.toLowerCase().endsWith(".jpeg"))
						testStepRow += getTestStepWithScreenshot(stepDescription, stepStatus, screenShotName);
					else
						testStepRow += getTestStepWithContentInPlaceOfScreenshot(stepDescription, stepStatus,
								screenShotName);
				} else {
					testStepRow += getTestStepWithoutScreenshot(stepDescription, stepStatus);
				}
				break;

			case PASS:
				if (reportSettings.shouldTakeScreenshotPassedStep()) {
					if (screenShotName.toLowerCase().endsWith(".png") || screenShotName.toLowerCase().endsWith(".jpg")
							|| screenShotName.toLowerCase().endsWith(".jpeg"))
						testStepRow += getTestStepWithScreenshot(stepDescription, stepStatus, screenShotName);
					else
						testStepRow += getTestStepWithContentInPlaceOfScreenshot(stepDescription, stepStatus,
								screenShotName);
				} else {
					testStepRow += getTestStepWithoutScreenshot(stepDescription, stepStatus);
				}
				break;

//			case SCREENSHOT:
//				testStepRow += getTestStepWithScreenshot(stepDescription, stepStatus, screenShotName);
//				break;
//
//			case DONE:
//				testStepRow += "\t\t\t\t\t <td class='justified'>" + stepDescription + "</td> \n" + "\t\t\t\t\t <td>"
//						+ stepStatus + "</td> \n" + "\t\t\t\t\t <td>" + "<small>"
//						+ Util.getCurrentFormattedTime(reportSettings.getDateFormatString()) + "</small>" + "</td> \n"
//						+ "\t\t\t\t\t <td>" + "" + "</td> \n" + "\t\t\t\t </tr> \n";
//				break;

			default:
				testStepRow += getTestStepWithoutScreenshot(stepDescription, stepStatus);
				break;
			}

			bufferedWriter.write(testStepRow);
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException("Error while updating HTML test log");
		}
    }
    
    private String getTestStepWithContentInPlaceOfScreenshot(String stepDescription, Status stepStatus,
			String screenShotName) {
		String testStepRow;

		if (reportSettings.shouldLinkScreenshotsToTestLog()) {
			testStepRow = "\t\t\t\t\t <td class='justified'>" + stepDescription + "</td> \n" + "\t\t\t\t\t <td class='"
					+ stepStatus.toString().toLowerCase() + "'>" + stepStatus + "</td> \n" + "\t\t\t\t\t <td>"
					+ "<small>" + Util.getCurrentFormattedTime(reportSettings.getDateFormatString()) + "</small>"
					+ "</td> \n" + "\t\t\t\t\t <td>" + screenShotName + "</td> \n" + "\t\t\t\t </tr> \n";
		} else {
			testStepRow = "\t\t\t\t\t <td class='justified'>" + stepDescription + "</td> \n" + "\t\t\t\t\t <td class='"
					+ stepStatus.toString().toLowerCase() + "'>" + stepStatus + "</td> \n" + "\t\t\t\t\t <td>"
					+ "<small>" + Util.getCurrentFormattedTime(reportSettings.getDateFormatString()) + "</small>"
					+ "</td> \n" + "\t\t\t\t\t <td>" + screenShotName + "</td> \n" + "\t\t\t\t </tr> \n";
		}

		return testStepRow;
	}
    
    private String getTestStepWithScreenshot(String stepDescription, Status stepStatus, String screenShotName) {
		String testStepRow;

		if (reportSettings.shouldLinkScreenshotsToTestLog()) {
			testStepRow = "\t\t\t\t\t <td class='justified'>" + stepDescription + "</td> \n" + "\t\t\t\t\t <td class='"
					+ stepStatus.toString().toLowerCase() + "'>" + stepStatus + "</td> \n" + "\t\t\t\t\t <td>"
					+ "<small>" + Util.getCurrentFormattedTime(reportSettings.getDateFormatString()) + "</small>"
					+ "</td> \n" + "\t\t\t\t\t <td>" + "<a href='..\\Screenshots\\" + screenShotName
					+ "'> <img src='..\\Screenshots\\" + screenShotName + "'>" + "</img> </a>" + "</td> \n"
					+ "\t\t\t\t </tr> \n";
		} else {
			testStepRow = "\t\t\t\t\t <td class='justified'>" + stepDescription + "</td> \n" + "\t\t\t\t\t <td class='"
					+ stepStatus.toString().toLowerCase() + "'>" + stepStatus + "</td> \n" + "\t\t\t\t\t <td>"
					+ "<small>" + Util.getCurrentFormattedTime(reportSettings.getDateFormatString()) + "</small>"
					+ "</td> \n" + "\t\t\t\t\t <td>" + " (Refer Screenshot @ " + screenShotName + ")" + "</td> \n"
					+ "\t\t\t\t </tr> \n";
		}

		return testStepRow;
	}

	private String getTestStepWithoutScreenshot(String stepDescription, Status stepStatus) {
		String testStepRow;

		testStepRow = "\t\t\t\t\t <td class='justified'>" + stepDescription + "</td> \n" + "\t\t\t\t\t <td class='"
				+ stepStatus.toString().toLowerCase() + "'>" + stepStatus + "</td> \n" + "\t\t\t\t\t <td>" + "<small>"
				+ Util.getCurrentFormattedTime(reportSettings.getDateFormatString()) + "</small>" + "</td> \n"
				+ "\t\t\t\t\t <td>" + " N/A " + "</td> \n" + "\t\t\t\t </tr> \n";

		return testStepRow;
	}

	public void addTestLogFooter(String executionTime, int nStepsPassed, int nStepsFailed) {
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(testLogPath, true));

			String testLogFooter = "\t\t\t </tbody> \n" + "\t\t </table> \n\n" +

					"\t\t <table id='footer'> \n" + "\t\t\t <colgroup> \n" + "\t\t\t\t <col style='width: 25%' /> \n"
					+ "\t\t\t\t <col style='width: 25%' /> \n" + "\t\t\t\t <col style='width: 25%' /> \n"
					+ "\t\t\t\t <col style='width: 25%' /> \n" + "\t\t\t </colgroup> \n\n" +

					"\t\t\t <tfoot> \n" + "\t\t\t\t <tr class='heading'> \n"
					+ "\t\t\t\t\t <th colspan='4'>Execution Duration: " + executionTime + "</th> \n"
					+ "\t\t\t\t </tr> \n" + "\t\t\t\t <tr class='subheading'> \n"
					+ "\t\t\t\t\t <td class='pass'>&nbsp;Steps passed</td> \n" + "\t\t\t\t\t <td class='pass'>&nbsp;: "
					+ nStepsPassed + "</td> \n" + "\t\t\t\t\t <td class='fail'>&nbsp;Steps failed</td> \n"
					+ "\t\t\t\t\t <td class='fail'>&nbsp;: " + nStepsFailed + "</td> \n" + "\t\t\t\t </tr> \n"
					+ "\t\t\t </tfoot> \n" + "\t\t </table> \n" + "\t </body> \n" + "</html>";

			bufferedWriter.write(testLogFooter);
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException("Error while adding footer to HTML test log");
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
