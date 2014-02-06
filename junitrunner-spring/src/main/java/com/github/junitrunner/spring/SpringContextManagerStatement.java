package com.github.junitrunner.spring;

import org.junit.runners.model.Statement;
import org.springframework.test.context.TestContextManager;

import com.github.junitrunner.javamethod.AroundStatement;

public class SpringContextManagerStatement extends AroundStatement {

    private final TestContextManager testContextManager;

    protected SpringContextManagerStatement(TestContextManager testContextManager, Statement next) {
        super(next);
        this.testContextManager = testContextManager;
    }

    @Override
    protected void before() throws Throwable {
        testContextManager.beforeTestClass();
    }

    @Override
    protected void after(Throwable failure) throws Throwable {
        testContextManager.afterTestClass();
    }
}
