package org.junitrunner;

import org.junit.internal.AssumptionViolatedException;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junitrunner.listener.BaseRunnerListener;

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
    public void testStarted(JUnitTask test) {
        runNotifier.fireTestStarted(test.describe());
    }

    @Override
    public void testFinished(JUnitTask test) {
        runNotifier.fireTestFinished(test.describe());
    }

    @Override
    public void testFailed(JUnitTask test, Throwable e) {
        Description description = test.describe();
        Failure failure = new Failure(description, e);
        if (e instanceof AssumptionViolatedException) {
            runNotifier.fireTestAssumptionFailed(failure);
        } else {
            runNotifier.fireTestFailure(failure);
        }
        runNotifier.fireTestFinished(description);
    }
}
