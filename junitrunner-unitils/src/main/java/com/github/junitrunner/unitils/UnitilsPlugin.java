package org.junitrunner.unitils;

import org.junit.runners.model.Statement;
import org.junitrunner.JUnitBasePlugin;
import org.junitrunner.JUnitRunner;
import org.junitrunner.JUnitTest;
import org.junitrunner.javamethod.JUnitJavaMethodTest;

public class UnitilsPlugin extends JUnitBasePlugin {

    public UnitilsPlugin(@SuppressWarnings("unused") Class<?> testClass) {
    }

    @Override
    public void plug(JUnitRunner jUnitRunner) {

        jUnitRunner.registerListener(new UnitilsRunnerListener());
    }

    @Override
    public Statement enhanceTest(JUnitTest test, Object testObject, Statement statement) {
        if (test instanceof JUnitJavaMethodTest) {
            return new UnitilsTestInterceptor((JUnitJavaMethodTest) test, testObject, statement);
        } else {
            return statement;
        }
    }
}
