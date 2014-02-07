package com.github.junitrunner.listener;

import org.junit.runner.Description;

import com.github.junitrunner.JUnitTask;

public class BaseRunnerListener implements RunnerListener {

    @Override
    public void testClassStarted(Description description) {
    }

    @Override
    public void testClassFinished(Description description) {
    }

    @Override
    public void taskStarted(JUnitTask task) {
    }

    @Override
    public void taskFinished(JUnitTask task, Throwable failure) {
    }

    @Override
    public void testIgnored(JUnitTask test) {
    }

    @Override
    public void testCoreStarted(JUnitTask test, Object testObject) {
    }

    @Override
    public void testCoreFinished(JUnitTask test, Object testObject, Throwable error) {
    }
}
