package org.junitrunner.listener;

import org.junit.runner.Description;
import org.junitrunner.JUnitTask;

public interface RunnerListener {

    void testClassStarted(Description description);

    void testClassFinished(Description description);

    void testSuiteStarted(JUnitTask test);

    void testSuiteFinished(JUnitTask test);

    void testCoreStarted(JUnitTask test, Object testObject);

    void testCoreFinished(JUnitTask test, Object testObject, Throwable error);

    void testStarted(JUnitTask test);

    void testFailed(JUnitTask test, Throwable e);

    void testFinished(JUnitTask test);

    void testIgnored(JUnitTask test);

}
