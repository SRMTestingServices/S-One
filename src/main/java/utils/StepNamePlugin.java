package utils;

import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;
import java.util.concurrent.atomic.AtomicReference;

public class StepNamePlugin implements ConcurrentEventListener {

    private static final ThreadLocal<AtomicReference<String>> currentStepName =
            ThreadLocal.withInitial(() -> new AtomicReference<>(""));

    private static final ThreadLocal<AtomicReference<Throwable>> currentStepError =
            ThreadLocal.withInitial(AtomicReference::new);

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestStepStarted.class, this::handleTestStepStarted);
        publisher.registerHandlerFor(TestStepFinished.class, this::handleTestStepFinished);
        publisher.registerHandlerFor(TestCaseFinished.class, this::handleTestCaseFinished);
    }

    private void handleTestStepStarted(TestStepStarted event) {
        if (event.getTestStep() instanceof PickleStepTestStep testStep) {
            currentStepName.get().set(testStep.getStep().getText());
            currentStepError.get().set(null); // Clear previous error
        }
    }

    private void handleTestStepFinished(TestStepFinished event) {
        if (event.getResult().getStatus() == Status.FAILED) {
            currentStepError.get().set(event.getResult().getError());
        }
    }

    private void handleTestCaseFinished(TestCaseFinished event) {
        // Clear thread-local data after test case completes
        clearCurrentStep();
    }

    public static String getCurrentStepName() {
        return currentStepName.get().get();
    }

    public static Throwable getCurrentStepError() {
        return currentStepError.get().get();
    }

    public static void clearCurrentStep() {
        currentStepName.get().set(null);
        currentStepError.get().set(null);
    }

    public static String getCurrentStepWithError() {
        String stepName = getCurrentStepName();
        Throwable error = getCurrentStepError();
        if (error != null) {
            return stepName + " [FAILED: " + error.getMessage() + "]";
        }
        return stepName;
    }
}