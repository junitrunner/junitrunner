package com.github.junitrunner;

import org.junit.internal.AssumptionViolatedException;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.MultipleFailureException;

import com.github.junitrunner.listener.BaseRunnerListener;

public class RunNotifierWrapper extends BaseRunnerListener {

    public final RunNotifier runNotifier;

    public RunNotifierWrapper(RunNotifier runNotifier) {
        this.runNotifier = runNotifier;
    }

    @Override
    public void taskIgnored(Task task) {
        runNotifier.fireTestIgnored(task.describe());
    }

    @Override
    public void taskStarted(Task task) {
        runNotifier.fireTestStarted(task.describe());
    }

    @Override
    public void taskFinished(Task task, Throwable failure) {
        if (failure != null) {
            fireFailure(task, failure);
        }
        runNotifier.fireTestFinished(task.describe());
    }

    private void fireFailure(Task task, Throwable failure) {

        Description description = task.describe();
        if (failure instanceof MultipleFailureException) {
            for (Throwable each : ((MultipleFailureException) failure).getFailures()) {
                fireFailure(task, each);
            }
        } else if (failure instanceof AssumptionViolatedException) {
            runNotifier.fireTestAssumptionFailed(new Failure(description, failure));
        } else {
            runNotifier.fireTestFailure(new Failure(description, failure));
        }
    }
}
