package org.junitrunner.javamethod;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

public class BeforeMethodStatement extends AroundStatement {

    private final FrameworkMethod frameworkMethod;
    private final Object testObject;

    public BeforeMethodStatement(Statement next, FrameworkMethod frameworkMethod, Object testObject) {
        super(next);
        this.frameworkMethod = frameworkMethod;
        this.testObject = testObject;
    }

    @Override
    protected void before() throws Throwable {
        frameworkMethod.invokeExplosively(testObject);
    }
}
