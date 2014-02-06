package com.github.junitrunner.unitils;

import org.junit.runners.model.Statement;
import org.unitils.core.TestListener;
import org.unitils.core.Unitils;

import com.github.junitrunner.javamethod.AroundStatement;
import com.github.junitrunner.javamethod.JUnitJavaMethodTest;

public class UnitilsTestInterceptor extends AroundStatement {

    private final JUnitJavaMethodTest test;
    private final Object testObject;
    private TestListener testListener = Unitils.getInstance().getTestListener();

    public UnitilsTestInterceptor(JUnitJavaMethodTest test, Object testObject, Statement statement) {
        super(statement);
        this.test = test;
        this.testObject = testObject;
    }

    @Override
    protected void before() throws Throwable {
        testListener.beforeTestSetUp(testObject, test.getMethod());
    }

    @Override
    protected void after(Throwable failure) throws Throwable {
        testListener.afterTestTearDown(testObject, test.getMethod());
    }
}