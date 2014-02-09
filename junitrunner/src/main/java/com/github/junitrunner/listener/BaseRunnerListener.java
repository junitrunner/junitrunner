package com.github.junitrunner.listener;

import org.junit.runner.Description;

import com.github.junitrunner.JUnitTask;
import com.github.junitrunner.Task;

public class BaseRunnerListener implements RunnerListener {

    @Override
    public void testClassStarted(Description description) {
    }

    @Override
    public void testClassFinished(Description description) {
    }

    @Override
    public void testCoreStarted(JUnitTask test, Object testObject) {
    }

    @Override
    public void testCoreFinished(JUnitTask test, Object testObject, Throwable error) {
    }

    @Override
    public void taskStarted(Task task) {
    }

    @Override
    public void taskFinished(Task task, Throwable failure) {
    }

    @Override
    public void taskIgnored(Task task) {
    }
}
