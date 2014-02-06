package org.junitrunner.spring;

import org.junit.runners.model.Statement;
import org.springframework.test.context.TestContextManager;

public class SpringPrepareTestInstanceStatement extends Statement {

    private final TestContextManager testContextManager;
    private final Object testObject;
    private final Statement next;

    public SpringPrepareTestInstanceStatement(TestContextManager testContextManager, Object testObject, Statement next) {
        this.testContextManager = testContextManager;
        this.testObject = testObject;
        this.next = next;
    }

    @Override
    public void evaluate() throws Throwable {
        testContextManager.prepareTestInstance(testObject);
        next.evaluate();
    }
}