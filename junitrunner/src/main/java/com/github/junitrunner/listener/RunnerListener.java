package com.github.junitrunner.listener;

import org.junit.runner.Description;

import com.github.junitrunner.JUnitTask;
import com.github.junitrunner.Task;

public interface RunnerListener {

    void testClassStarted(Description description);

    void testClassFinished(Description description);

    void testCoreStarted(JUnitTask test, Object testObject);

    void testCoreFinished(JUnitTask test, Object testObject, Throwable error);

    void taskStarted(Task task);

    void taskFinished(Task task, Throwable failure);

    void taskIgnored(Task task);
}
