package com.github.junitrunner.spring;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.internal.runners.statements.ExpectException;
import org.junit.internal.runners.statements.FailOnTimeout;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.springframework.test.annotation.ProfileValueUtils;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.annotation.Timed;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.junit4.statements.RunAfterTestMethodCallbacks;
import org.springframework.test.context.junit4.statements.RunBeforeTestMethodCallbacks;
import org.springframework.test.context.junit4.statements.SpringFailOnTimeout;
import org.springframework.test.context.junit4.statements.SpringRepeat;

import com.github.junitrunner.JUnitBasePlugin;
import com.github.junitrunner.JUnitRunner;
import com.github.junitrunner.JUnitSuite;
import com.github.junitrunner.JUnitTest;
import com.github.junitrunner.javamethod.JUnitJavaMethodTest;

public class SpringPlugin extends JUnitBasePlugin {

    private static Log logger = LogFactory.getLog(SpringPlugin.class);

    private final Class<?> testClass;

    private TestContextManager testContextManager;

    public SpringPlugin(Class<?> testClass) {
        this.testClass = testClass;
    }

    @Override
    public void plug(JUnitRunner jUnitRunner) {

        testContextManager = new TestContextManager(testClass, null);
    }

    @Override
    public Statement enhanceClassStatement(Statement statement, Description description, List<Throwable> errors) {

        return new SpringContextManagerStatement(testContextManager, statement);
    }

    @Override
    public Statement enhanceTestCore(JUnitTest test, Object testObject, Statement statement) {

        Test testAnnotation = test.getAnnotation(Test.class);
        Class<? extends Throwable> junitExpectedException = (testAnnotation != null && testAnnotation.expected() != Test.None.class ? testAnnotation
                .expected() : null);

        @SuppressWarnings("deprecation")
        org.springframework.test.annotation.ExpectedException expectedExAnn = test
                .getAnnotation(org.springframework.test.annotation.ExpectedException.class);
        @SuppressWarnings("deprecation")
        Class<? extends Throwable> springExpectedException = (expectedExAnn != null ? expectedExAnn.value() : null);

        if (springExpectedException != null && junitExpectedException != null) {
            JUnitJavaMethodTest methodTest = (JUnitJavaMethodTest) test;
            String msg = "Test method [" + methodTest.getMethod() + "] has been configured with Spring's @ExpectedException("
                    + springExpectedException.getName() + ".class) and JUnit's @Test(expected=" + junitExpectedException.getName()
                    + ".class) annotations. " + "Only one declaration of an 'expected exception' is permitted per test method.";
            logger.error(msg);
            throw new IllegalStateException(msg);
        }

        return springExpectedException != null ? new ExpectException(statement, springExpectedException) : statement;
    }

    @Override
    public Statement enhanceTest(JUnitTest test, Object testObject, Statement statement) {

        if (test instanceof JUnitJavaMethodTest) {
            JUnitJavaMethodTest methodTest = (JUnitJavaMethodTest) test;

            statement = new RunBeforeTestMethodCallbacks(statement, testObject, methodTest.getMethod(), testContextManager);
            statement = new RunAfterTestMethodCallbacks(statement, testObject, methodTest.getMethod(), testContextManager);
            statement = withPotentialRepeat(methodTest, statement);
            statement = withPotentialTimeout(methodTest, statement);
        }

        statement = new SpringPrepareTestInstanceStatement(testContextManager, testObject, statement);
        return statement;
    }

    @Override
    public void configureSuite(JUnitSuite suite, List<Throwable> errors) {

        if (!ProfileValueUtils.isTestEnabledInThisEnvironment(testClass)) {
            suite.ignore();
        }
    }

    @Override
    public void configureTest(JUnitTest test, List<Throwable> errors) {

        if (test instanceof JUnitJavaMethodTest) {
            JUnitJavaMethodTest methodTest = (JUnitJavaMethodTest) test;
            Method method = methodTest.getMethod();
            if (!ProfileValueUtils.isTestEnabledInThisEnvironment(method, testClass)) {
                test.ignore();
            }
        }
    }

    private Statement withPotentialRepeat(JUnitJavaMethodTest methodTest, Statement next) {

        Repeat repeatAnnotation = methodTest.getAnnotation(Repeat.class);
        int repeat = (repeatAnnotation != null ? repeatAnnotation.value() : 1);
        return new SpringRepeat(next, methodTest.getMethod(), repeat);
    }

    private Statement withPotentialTimeout(JUnitJavaMethodTest methodTest, Statement next) {

        Statement statement = null;
        long springTimeout = getSpringTimeout(methodTest);
        long junitTimeout = getJUnitTimeout(methodTest);
        if (springTimeout > 0 && junitTimeout > 0) {
            String msg = "Test method [" + methodTest.getMethod() + "] has been configured with Spring's @Timed(millis=" + springTimeout
                    + ") and JUnit's @Test(timeout=" + junitTimeout
                    + ") annotations. Only one declaration of a 'timeout' is permitted per test method.";
            logger.error(msg);
            throw new IllegalStateException(msg);
        } else if (springTimeout > 0) {
            statement = new SpringFailOnTimeout(next, springTimeout);
        } else if (junitTimeout > 0) {
            statement = new FailOnTimeout(next, junitTimeout);
        } else {
            statement = next;
        }

        return statement;
    }

    private long getJUnitTimeout(JUnitJavaMethodTest methodTest) {
        Test testAnnotation = methodTest.getAnnotation(Test.class);
        return (testAnnotation != null && testAnnotation.timeout() > 0 ? testAnnotation.timeout() : 0);
    }

    private long getSpringTimeout(JUnitJavaMethodTest methodTest) {
        Timed timedAnnotation = methodTest.getAnnotation(Timed.class);
        return (timedAnnotation != null && timedAnnotation.millis() > 0 ? timedAnnotation.millis() : 0);
    }
}
