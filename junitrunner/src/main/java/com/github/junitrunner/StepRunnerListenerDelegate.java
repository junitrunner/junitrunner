package com.github.junitrunner;

import com.github.junitrunner.listener.RunnerListenerContainer;

public class StepRunnerListenerDelegate {

    private final RunnerListenerContainer listenerContainer;

    public StepRunnerListenerDelegate(RunnerListenerContainer listenerContainer) {
        this.listenerContainer = listenerContainer;
    }

    public void stepIgnored(Task step) {
        listenerContainer.taskIgnored(step);
    }

    public void stepStarted(Task step) {
        listenerContainer.taskStarted(step);
    }

    public void stepFinished(Task step, Throwable failure) {
        listenerContainer.taskFinished(step, failure);
    }
}
