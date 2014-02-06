package com.github.junitrunner.spring;

import java.util.ArrayList;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runner.notification.StoppedByUserException;

final class MockRunNotifier extends RunNotifier {

    private final List<String> calls = new ArrayList<String>();

    MockRunNotifier() {
    }

    public List<String> getCalls() {
        return calls;
    }

    @Override
    public void fireTestRunStarted(Description description) {
        calls.add("fireTestRunStarted");
    }

    @Override
    public void fireTestRunFinished(Result result) {
        calls.add("fireTestRunFinished");
    }

    @Override
    public void fireTestStarted(Description description) throws StoppedByUserException {
        calls.add("fireTestStarted");
    }

    @Override
    public void fireTestFailure(Failure failure) {
        calls.add("fireTestFailure");
    }

    @Override
    public void fireTestAssumptionFailed(Failure failure) {
        calls.add("fireTestFailure");
    }

    @Override
    public void fireTestIgnored(Description description) {
        calls.add("fireTestIgnored");
    }

    @Override
    public void fireTestFinished(Description description) {
        calls.add("fireTestFinished");
    }

    @Override
    public void pleaseStop() {
        calls.add("pleaseStop");
    }
}
