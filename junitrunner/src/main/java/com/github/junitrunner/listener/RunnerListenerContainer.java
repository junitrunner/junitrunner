package com.github.junitrunner.listener;

import java.util.ArrayList;
import java.util.List;

import org.junit.runner.Description;

import com.github.junitrunner.Task;
import com.github.junitrunner.JUnitTask;

public class RunnerListenerContainer {// implements RunnerListener {

    private final List<RunnerListener> listeners = new ArrayList<RunnerListener>();

    public void addListener(RunnerListener listener) {
        listeners.add(listener);
    }

    public void testClassStarted(Description description) {
        for (RunnerListener listener : listeners) {
            listener.testClassStarted(description);
        }
    }

    public void testClassFinished(Description description) {
        for (RunnerListener listener : listeners) {
            listener.testClassFinished(description);
        }
    }

    public void testCoreStarted(JUnitTask test, Object testObject) {
        for (RunnerListener listener : listeners) {
            listener.testCoreStarted(test, testObject);
        }
    }

    public void testCoreFinished(JUnitTask test, Object testObject, Throwable error) {
        for (RunnerListener listener : listeners) {
            listener.testCoreFinished(test, testObject, error);
        }
    }

    public void taskIgnored(Task task) {
        for (RunnerListener listener : listeners) {
            listener.taskIgnored(task);
        }
    }

    public void taskStarted(Task task) {
        for (RunnerListener listener : listeners) {
            listener.taskStarted(task);
        }
    }

    public void taskFinished(Task task, Throwable failure) {
        for (RunnerListener listener : listeners) {
            listener.taskFinished(task, failure);
        }
    }
}
