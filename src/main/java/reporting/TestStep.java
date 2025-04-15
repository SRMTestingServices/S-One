package reporting;

public class TestStep {
	private String stepNumber;
	private String stepName;
	private String stepDescription;
	private Status status;
	private String screenshot;
	private String errorMessage;
	private long executionTime; // milliseconds

	// Constructors
	public TestStep(String stepNumber, String stepName, String stepDescription,
					Status status, String screenshot) {
		this(stepNumber, stepName, stepDescription, status, screenshot, null, 0);
	}

	public TestStep(String stepNumber, String stepName, String stepDescription,
					Status status, String screenshot, String errorMessage, long executionTime) {
		this.stepNumber = stepNumber;
		this.stepName = stepName;
		this.stepDescription = stepDescription;
		this.status = status;
		this.screenshot = screenshot;
		this.errorMessage = errorMessage;
		this.executionTime = executionTime;
	}

	// Getters
	public String getStepNumber() { return stepNumber; }
	public String getStepName() { return stepName; }
	public String getStepDescription() { return stepDescription; }
	public Status getStatus() { return status; }
	public String getScreenshot() { return screenshot; }
	public String getErrorMessage() { return errorMessage; }
	public long getExecutionTime() { return executionTime; }
}