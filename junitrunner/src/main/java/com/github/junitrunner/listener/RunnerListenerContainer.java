package com.github.junitrunner.listener;

import java.util.ArrayList;
import java.util.List;

import org.junit.runner.Description;

import com.github.junitrunner.JUnitSuite;
import com.github.junitrunner.JUnitTask;
import com.github.junitrunner.JUnitTest;

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

    public void taskIgnored(JUnitTask test) {
        for (RunnerListener listener : listeners) {
            listener.testIgnored(test);
        }
    }

    public void taskStarted(JUnitTask task) {
        if (task instanceof JUnitSuite) {
            testSuiteStarted(task);
        } else {
            testStarted(task);
        }
    }

    public void taskFailed(JUnitTask task, Throwable exception) {
        if (task instanceof JUnitTest) {
            testFailed(task, exception);
        }
    }

    public void taskFinished(JUnitTask task) {
        if (task instanceof JUnitSuite) {
            testSuiteFinished(task);
        } else {
            testFinished(task);
        }
    }

    private void testSuiteStarted(JUnitTask test) {
        for (RunnerListener listener : listeners) {
            listener.testSuiteStarted(test);
        }
    }

    private void testSuiteFinished(JUnitTask test) {
        for (RunnerListener listener : listeners) {
            listener.testSuiteFinished(test);
        }
    }

    private void testStarted(JUnitTask test) {
        for (RunnerListener listener : listeners) {
            listener.testStarted(test);
        }
    }

    private void testFailed(JUnitTask test, Throwable e) {
        for (RunnerListener listener : listeners) {
            listener.testFailed(test, e);
        }
    }

    private void testFinished(JUnitTask test) {
        for (RunnerListener listener : listeners) {
            listener.testFinished(test);
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
}
