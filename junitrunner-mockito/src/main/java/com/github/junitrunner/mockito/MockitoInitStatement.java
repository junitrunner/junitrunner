package com.github.junitrunner.mockito;

import org.junit.runners.model.Statement;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class MockitoInitStatement extends Statement {

    private final Statement statement;
    private final Object testObject;

    public MockitoInitStatement(Statement statement, Object testObject) {
        this.statement = statement;
        this.testObject = testObject;
    }

    @Override
    public void evaluate() throws Throwable {
        MockitoAnnotations.initMocks(testObject);
        statement.evaluate();
        Mockito.validateMockitoUsage();
    }
}