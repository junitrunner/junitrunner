package org.junitrunner.plugin.annotation;

import java.util.List;

import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.junitrunner.JUnitTest;

public interface TestRuleFactory {

    void validateTestRules(Class<?> testClass, List<Throwable> errors);

    Statement addTestParts(JUnitTest test, Object testObject, Statement statement);

    Statement addTestInterceptors(JUnitTest test, Object testObject, Statement statement, Description description, Class<?> testClass);
}
