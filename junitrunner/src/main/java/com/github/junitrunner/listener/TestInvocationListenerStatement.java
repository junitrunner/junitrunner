package com.github.junitrunner.listener;

import org.junit.runners.model.Statement;

import com.github.junitrunner.JUnitTask;
import com.github.junitrunner.javamethod.AroundStatement;

public class TestInvocationListenerStatement extends AroundStatement {

    private final JUnitTask test;
    private final Object testObject;
    private final RunnerListenerContainer listenerContainer;

    public TestInvocationListenerStatement(JUnitTask test, Statement next, Object testObject, RunnerListenerContainer listenerContainer) {
        super(next);
        this.test = test;
        this.testObject = testObject;
        this.listenerContainer = listenerContainer;
    }

    @Override
    protected void before() throws Throwable {
        listenerContainer.testCoreStarted(test, testObject);
    }

    @Override
    protected void after(Throwable failure) throws Throwable {
        listenerContainer.testCoreFinished(test, testObject, failure);
    }
}