package org.junitrunner.spring;

import org.junit.runners.model.Statement;
import org.junitrunner.javamethod.AroundStatement;
import org.springframework.test.context.TestContextManager;

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
