package com.github.junitrunner.plugin.annotation;

import java.util.List;

import com.github.junitrunner.JUnitTask;

public interface TestFactory {

    List<JUnitTask> createTests(Class<?> testClass, List<Throwable> errors);
}
