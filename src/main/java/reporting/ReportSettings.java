package reporting;

import java.io.File;
import java.text.SimpleDateFormat;

/**
 * Class to encapsulate the reporting settings in the framework
 * 
 * @author Cognizant
 */
public class ReportSettings {
	private final String reportPath;
	private final String reportName;

	private String projectName;
	private int logLevel;
	private String dateFormatString;

	private boolean generateExcelReports;
	private boolean generateHtmlReports;

	private boolean takeScreenshotFailedStep;
	private boolean takeScreenshotPassedStep;

	private boolean linkScreenshotsToTestLog;
	private boolean linkTestLogsToSummary;

	private boolean consolidateScreenshotsInWordDoc;
	private boolean consolidateScreenshotsInPDF;

	private boolean generateSeeTestReports;
	private boolean generatePerfectoReports;

	private boolean isMobileExecution;

	/**
	 * Constructor to initialize the report settings
	 * 
	 * @param reportPath
	 *            The report path
	 * @param reportName
	 *            The report name
	 */
	
	
	public ReportSettings(String reportPath, String reportName) {
		boolean reportPathExists = new File(reportPath).isDirectory();
		if (!reportPathExists) {
			throw new FrameworkException(
					"The given report path does not exist!");
		}
		this.reportPath = reportPath;
		this.reportName = reportName;

		// Set default values for all the report settings
		projectName = "";
		logLevel = 4;
		generateExcelReports = true;
		generateHtmlReports = true;
		generateSeeTestReports = false;
		generatePerfectoReports = false;
		takeScreenshotFailedStep = true;
		takeScreenshotPassedStep = true;
		linkScreenshotsToTestLog = true;
		linkTestLogsToSummary = true;
		consolidateScreenshotsInWordDoc = false;
		consolidateScreenshotsInPDF = true;
		dateFormatString = "MMM-dd-yyyy hh:mm:ss a z";
		isMobileExecution = false;
	}

	/**
	 * Function to get the absolute path where the report is to be stored
	 * 
	 * @return The report path
	 */
	public String getReportPath() {
		return reportPath;
	}

	/**
	 * Function to get the name of the report
	 * 
	 * @return The report name
	 */
	public String getReportName() {
		return reportName;
	}

	/**
	 * Function to get the name of the project being automated
	 * 
	 * @return The project name
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * Function to set the name of the project being automated
	 * 
	 * @param projectName
	 *            The project name
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/**
	 * Function to get the logging level of the reports. Log levels range
	 * between 0 to 5, with 0 being minimal reporting and 5 being highly
	 * detailed reporting
	 * 
	 * @return The log level
	 */
	public int getLogLevel() {
		return logLevel;
	}

	/**
	 * Function to set the logging level of the reports. Log levels range
	 * between 0 to 5, with 0 being minimal reporting and 5 being highly
	 * detailed reporting
	 * 
	 * @param logLevel
	 *            The log level
	 */
	public void setLogLevel(int logLevel) {
		this.logLevel = logLevel;

		if (logLevel < 0) {
			this.logLevel = 0;
		}

		if (logLevel > 5) {
			this.logLevel = 5;
		}
	}

	/**
	 * Function to get a string indicating the format for the date/time to be
	 * used within the report
	 * 
	 * @return The date/time formatting string
	 * @see SimpleDateFormat
	 */
	public String getDateFormatString() {
		return dateFormatString;
	}

	/**
	 * Function to set a string indicating the format for the date/time to be
	 * used within the report
	 * 
	 * @param dateFormatString
	 *            The date/time formatting string
	 * @see SimpleDateFormat
	 */
	public void setDateFormatString(String dateFormatString) {
		this.dateFormatString = dateFormatString;
	}

	/**
	 * Function to get a Boolean value indicating whether Excel reports should
	 * be generated
	 * 
	 * @return Boolean value indicating whether Excel reports should be
	 *         generated
	 */
	public boolean shouldGenerateExcelReports() {
		return generateExcelReports;
	}

	/**
	 * Function to set a Boolean value indicating whether Excel reports should
	 * be generated
	 * 
	 * @param generateExcelReports
	 *            Boolean value indicating whether Excel reports should be
	 *            generated
	 */
	public void setGenerateExcelReports(boolean generateExcelReports) {
		this.generateExcelReports = generateExcelReports;
	}

	/**
	 * Function to get a Boolean value indicating whether HTML reports should be
	 * generated
	 * 
	 * @return Boolean value indicating whether HTML reports should be generated
	 */
	public boolean shouldGenerateHtmlReports() {
		return generateHtmlReports;
	}

	/**
	 * Function to set a Boolean value indicating whether HTML reports should be
	 * generated
	 * 
	 * @param generateHtmlReports
	 *            Boolean value indicating whether HTML reports should be
	 *            generated
	 */
	public void setGenerateHtmlReports(boolean generateHtmlReports) {
		this.generateHtmlReports = generateHtmlReports;
	}

	/**
	 * Function to get a Boolean value indicating whether SeeTest reports should
	 * be generated
	 * 
	 * @return Boolean value indicating whether SeeTest reports should be
	 *         generated
	 */
	public boolean shouldGenerateSeeTestReports() {
		return generateSeeTestReports;
	}

	/**
	 * Function to set a Boolean value indicating whether SeeTest reports should
	 * be generated
	 * 
	 * @param generateSeeTestReports
	 *            Boolean value indicating whether SeeTest reports should be
	 *            generated
	 */
	public void setGenerateSeeTestReports(boolean generateSeeTestReports) {
		this.generateSeeTestReports = generateSeeTestReports;
	}

	/**
	 * Function to get a Boolean value indicating whether Perfecto reports
	 * should be generated
	 * 
	 * @return Boolean value indicating whether Perfecto reports should be
	 *         generated
	 */
	public boolean shouldGeneratePerfectoReports() {
		return generatePerfectoReports;
	}

	/**
	 * Function to set a Boolean value indicating whether Perfecto reports
	 * should be generated
	 * 
	 * @param generatePerfectoReports
	 *            Boolean value indicating whether Perfecto reports should be
	 *            generated
	 */
	public void setGeneratePerfectoReports(boolean generatePerfectoReports) {
		this.generatePerfectoReports = generatePerfectoReports;
	}

	/**
	 * Function to get a Boolean value indicating whether a screenshot should be
	 * captured for failed steps
	 * 
	 * @return Boolean value indicating whether a screenshot should be captured
	 *         for failed steps
	 */
	public boolean shouldTakeScreenshotFailedStep() {
		return takeScreenshotFailedStep;
	}

	/**
	 * Function to set a Boolean value indicating whether a screenshot should be
	 * captured for failed steps
	 * 
	 * @param takeScreenshotFailedStep
	 *            Boolean value indicating whether a screenshot should be
	 *            captured for failed steps
	 */
	public void setTakeScreenshotFailedStep(boolean takeScreenshotFailedStep) {
		this.takeScreenshotFailedStep = takeScreenshotFailedStep;
	}

	/**
	 * Function to get a Boolean value indicating whether a screenshot should be
	 * captured for passed steps
	 * 
	 * @return Boolean value indicating whether a screenshot should be captured
	 *         for passed steps
	 */
	public boolean shouldTakeScreenshotPassedStep() {
		return takeScreenshotPassedStep;
	}

	/**
	 * Function to set a Boolean value indicating whether a screenshot should be
	 * captured for passed steps
	 * 
	 * @param takeScreenshotPassedStep
	 *            Boolean value indicating whether a screenshot should be
	 *            captured for passed steps
	 */
	public void setTakeScreenshotPassedStep(boolean takeScreenshotPassedStep) {
		this.takeScreenshotPassedStep = takeScreenshotPassedStep;
	}

	/**
	 * Function to get a Boolean value indicating whether any screenshot taken
	 * must be linked to the corresponding step within the test log
	 * 
	 * @return Boolean value indicating whether any screenshot taken must be
	 *         linked to the corresponding step within the test log
	 */
	public boolean shouldLinkScreenshotsToTestLog() {
		return linkScreenshotsToTestLog;
	}

	/**
	 * Function to set a Boolean value indicating whether any screenshot taken
	 * must be linked to the corresponding step within the test log
	 * 
	 * @param linkScreenshotsToTestLog
	 *            Boolean value indicating whether any screenshot taken must be
	 *            linked to the corresponding step within the test log
	 */
	public void setLinkScreenshotsToTestLog(boolean linkScreenshotsToTestLog) {
		this.linkScreenshotsToTestLog = linkScreenshotsToTestLog;
	}

	/**
	 * Function to get a Boolean value indicating whether the individual test
	 * logs must be linked to the result summary
	 * 
	 * @return Boolean value indicating whether the individual test logs must be
	 *         linked to the result summary
	 */
	public boolean shouldLinkTestLogsToSummary() {
		return linkTestLogsToSummary;
	}

	/**
	 * Function to set a Boolean value indicating whether the individual test
	 * logs must be linked to the result summary
	 * 
	 * @param linkTestLogsToSummary
	 *            Boolean value indicating whether the individual test logs must
	 *            be linked to the result summary
	 */
	public void setLinkTestLogsToSummary(boolean linkTestLogsToSummary) {
		this.linkTestLogsToSummary = linkTestLogsToSummary;
	}

	/**
	 * Function to get a Boolean value indicating whether all the screenshots
	 * must be consolidated into a Word document
	 * 
	 * @return Boolean value indicating whether all the screenshots must be
	 *         consolidated into a Word document
	 */
	public boolean shouldConsolidateScreenshotsInWordDoc() {
		return consolidateScreenshotsInWordDoc;
	}

	/**
	 * Function to set a Boolean value indicating whether all the screenshots
	 * must be consolidated into a Word document
	 * 
	 * @param consolidateScreenshotsInWordDoc
	 *            Boolean value indicating whether all the screenshots must be
	 *            consolidated into a Word document
	 */
	public void setConsolidateScreenshotsInWordDoc(
			boolean consolidateScreenshotsInWordDoc) {
		this.consolidateScreenshotsInWordDoc = consolidateScreenshotsInWordDoc;
	}

	/**
	 * Function to get a Boolean value indicating whether all the screenshots
	 * must be consolidated into a PDF document
	 * 
	 * @return Boolean value indicating whether all the screenshots must be
	 *         consolidated into a PDF document
	 */
	public boolean shouldConsolidateScreenshotsInPDF() {
		return consolidateScreenshotsInPDF;
	}

	/**
	 * Function to set a Boolean value indicating whether all the screenshots
	 * must be consolidated into a PDF document
	 * 
	 * @param consolidateScreenshotsInPDF
	 *            Boolean value indicating whether all the screenshots must be
	 *            consolidated into a PDF document
	 */
	public void setConsolidateScreenshotsInPDF(
			boolean consolidateScreenshotsInPDF) {
		this.consolidateScreenshotsInPDF = consolidateScreenshotsInPDF;
	}

	public void setisMobileExecution(boolean isMobileExecution) {
		this.isMobileExecution = isMobileExecution;
	}

	public String getWidth() {
		String width = "";
		if (this.isMobileExecution) {
			width = "250px";
		} else {
			width = "325px";
		}
		return width;
	}

	public String getHeight() {
		String height = "";
		if (this.isMobileExecution) {
			height = "450px";
		} else {
			height = "250px";
		}
		return height;
	}

}