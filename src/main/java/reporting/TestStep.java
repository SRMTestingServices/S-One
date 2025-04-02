package reporting;

public class TestStep {
	String stepNumber;
	String stepName;
	String stepDescription;
	Status status;
	String screenshot;

	public TestStep(String stepNumber, String stepName, String stepDescription, Status status, String screenshot) {
		this.stepNumber = stepNumber;
		this.stepName = stepName;
		this.stepDescription = stepDescription;
		this.status = status;
		this.screenshot = screenshot;
	}
}
