package reporting;

import lombok.Getter;

public class TestStep {
    // Getters
    @Getter
    private String stepNumber;
	@Getter
    private String stepDescription;
	@Getter
    private Status status;
	@Getter
    private String screenshot;
	@Getter
    private String errorMessage;
	@Getter
    private long executionTime; // milliseconds
	@Getter
	private String screenshotFilename;

	public TestStep(String stepNumber, String stepDescription,
					Status status, String screenshotFullPath, String errorMessage,
					long executionTime) {
		this.stepNumber = stepNumber;
		this.stepDescription = stepDescription;
		this.status = status;
		this.errorMessage = errorMessage;
		this.executionTime = executionTime;

		// Process the screenshot path
		if (screenshotFullPath != null && !screenshotFullPath.isEmpty()) {
			this.screenshotFilename = extractFilename(screenshotFullPath);
			this.screenshot = convertToRelativePath(screenshotFullPath);
		}
	}

	private String extractFilename(String path) {
		if (path == null) return null;
		// Handle both forward and backward slashes
		return path.replace("\\", "/").substring(path.lastIndexOf('/') + 1);
	}

	private String convertToRelativePath(String fullPath) {
		// Convert to relative path from test-reports folder
		return fullPath.replace("\\", "/")
				.replace("file:///", "")
				.replace(ReportManager.getCurrentReportDirectory(), "")
				.replace("test-reports/", "");
	}

}