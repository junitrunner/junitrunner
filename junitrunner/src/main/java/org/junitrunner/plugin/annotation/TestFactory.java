package org.junitrunner.plugin.annotation;

import java.util.List;

import org.junitrunner.JUnitTask;

public interface TestFactory {

    List<JUnitTask> createTests(Class<?> testClass, List<Throwable> errors);
}
