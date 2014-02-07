package com.github.junitrunner.listener;

import org.junit.runner.Description;

import com.github.junitrunner.JUnitTask;

public interface RunnerListener {

    void testClassStarted(Description description);

    void testClassFinished(Description description);

    void taskStarted(JUnitTask task);

    void taskFinished(JUnitTask task, Throwable failure);

    void testCoreStarted(JUnitTask test, Object testObject);

    void testCoreFinished(JUnitTask test, Object testObject, Throwable error);

    void testIgnored(JUnitTask test);

}
