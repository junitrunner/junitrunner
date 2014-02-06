package org.junitrunner;

import java.util.List;

import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public abstract class JUnitBasePlugin implements JUnitPlugin {

    @Override
    public void plug(JUnitRunner jUnitRunner) {
    }

    @Override
    public Statement enhanceClassStatement(Statement statement, Description description, List<Throwable> errors) {
        return statement;
    }

    @Override
    public void validateTestRules(List<Throwable> errors) {
    }

    @Override
    public List<JUnitTask> createTasks(List<Throwable> errors) {
        return null;
    }

    @Override
    public Statement enhanceTestCore(JUnitTest test, Object testObject, Statement statement) {
        return statement;
    }

    @Override
    public Statement enhanceTest(JUnitTest test, Object testObject, Statement statement) {
        return statement;
    }

    @Override
    public void configureSuite(JUnitSuite suite, List<Throwable> errors) {
    }

    @Override
    public void configureTest(JUnitTest test, List<Throwable> errors) {
    }
}
