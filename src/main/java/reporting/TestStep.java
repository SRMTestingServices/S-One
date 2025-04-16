package reporting;

public class TestStep {
	private String stepNumber;
	private String stepDescription;
	private Status status;
	private String screenshot;
	private String errorMessage;
	private long executionTime; // milliseconds
	private String stepType; // "Given", "When", "Then", or "Dynamic"

	// Constructors
	public TestStep(String stepNumber,  String stepDescription,
					Status status, String screenshot) {
		this(stepNumber, stepDescription, status, screenshot, null, 0);
	}

	public TestStep(String stepNumber, String stepDescription,
					Status status, String screenshot, String errorMessage, long executionTime) {
		this.stepNumber = stepNumber;
		this.stepDescription = stepDescription;
		this.status = status;
		this.screenshot = screenshot;
		this.errorMessage = errorMessage;
		this.executionTime = executionTime;
	}

	// Getters
	public String getStepNumber() { return stepNumber; }
	public String getStepDescription() { return stepDescription; }
	public Status getStatus() { return status; }
	public String getScreenshot() { return screenshot; }
	public String getErrorMessage() { return errorMessage; }
	public long getExecutionTime() { return executionTime; }
}