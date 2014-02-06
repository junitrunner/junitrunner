package com.github.junitrunner.javamethod;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

public final class AfterMethodStatement extends AroundStatement {

    private Object testObject;
    private FrameworkMethod frameworkMethod;

    public AfterMethodStatement(Object testObject, FrameworkMethod frameworkMethod, Statement next) {
        super(next);
        this.testObject = testObject;
        this.frameworkMethod = frameworkMethod;
    }

    @Override
    protected void after(Throwable failure) throws Throwable {
        frameworkMethod.invokeExplosively(testObject);
    }
}