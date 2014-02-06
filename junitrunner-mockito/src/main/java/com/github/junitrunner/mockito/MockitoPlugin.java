package com.github.junitrunner.mockito;

import org.junit.runners.model.Statement;

import com.github.junitrunner.JUnitBasePlugin;
import com.github.junitrunner.JUnitTest;

public class MockitoPlugin extends JUnitBasePlugin {

    @SuppressWarnings("unused")
    public MockitoPlugin(Class<?> testClass) {
    }

    public Statement enhanceTest(JUnitTest test, final Object testObject, final Statement statement) {
        return new MockitoInitStatement(statement, testObject);
    }
}
