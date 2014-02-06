package com.github.junitrunner;

import java.util.List;

import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public interface JUnitPlugin {

    void plug(JUnitRunner jUnitRunner);

    Statement enhanceClassStatement(Statement statement, Description description, List<Throwable> errors);

    void validateTestRules(List<Throwable> errors);

    List<JUnitTask> createTasks(List<Throwable> errors);

    Statement enhanceTestCore(JUnitTest test, Object testObject, Statement statement);

    Statement enhanceTest(JUnitTest test, Object testObject, Statement statement);

    void configureSuite(JUnitSuite suite, List<Throwable> errors);

    void configureTest(JUnitTest test, List<Throwable> errors);
}
