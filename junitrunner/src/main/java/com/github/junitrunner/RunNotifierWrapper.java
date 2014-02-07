package com.github.junitrunner;

import org.junit.internal.AssumptionViolatedException;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

import com.github.junitrunner.listener.BaseRunnerListener;

public class RunNotifierWrapper extends BaseRunnerListener {

    public final RunNotifier runNotifier;

    public RunNotifierWrapper(RunNotifier runNotifier) {
        this.runNotifier = runNotifier;
    }

    @Override
    public void testIgnored(JUnitTask test) {
        runNotifier.fireTestIgnored(test.describe());
    }

    @Override
    public void taskStarted(JUnitTask task) {
        if (task instanceof JUnitTest) {
            runNotifier.fireTestStarted(task.describe());
        }
    }

    @Override
    public void taskFinished(JUnitTask task, Throwable failure) {
        if (task instanceof JUnitTest) {
            if (failure != null) {
                Description description = task.describe();
                if (failure instanceof AssumptionViolatedException) {
                    runNotifier.fireTestAssumptionFailed(new Failure(description, failure));
                } else {
                    runNotifier.fireTestFailure(new Failure(description, failure));
                }
            }

            runNotifier.fireTestFinished(task.describe());
        }
    }
}
