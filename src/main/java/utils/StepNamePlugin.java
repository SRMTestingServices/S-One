package utils;

import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;

public class StepNamePlugin implements ConcurrentEventListener {

    private static final ThreadLocal<String> currentStepName = new ThreadLocal<>();
    private static final ThreadLocal<Throwable> currentStepError = new ThreadLocal<>();

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestStepStarted.class, this::handleTestStepStarted);
        publisher.registerHandlerFor(TestStepFinished.class, this::handleTestStepFinished);
    }

    private void handleTestStepStarted(TestStepStarted event) {
        if (event.getTestStep() instanceof PickleStepTestStep testStep) {
            currentStepName.set(testStep.getStep().getText());
        }
    }

    private void handleTestStepFinished(TestStepFinished event) {
        if (event.getResult().getStatus() == Status.FAILED) {
            currentStepError.set(event.getResult().getError());
        }
    }

    public static String getCurrentStepName() {
        return currentStepName.get();
    }

    public static Throwable getCurrentStepError() {
        return currentStepError.get();
    }
}