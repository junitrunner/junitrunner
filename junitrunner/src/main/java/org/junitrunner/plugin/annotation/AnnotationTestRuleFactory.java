package org.junitrunner.plugin.annotation;

import static org.junit.internal.runners.rules.RuleFieldValidator.*;

import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.Test.None;
import org.junit.internal.runners.statements.FailOnTimeout;
import org.junit.rules.ExpectedException;
import org.junit.rules.MethodRule;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.junitrunner.JUnitTest;
import org.junitrunner.TestHelper;
import org.junitrunner.javamethod.AfterMethodStatement;
import org.junitrunner.javamethod.BeforeMethodStatement;
import org.junitrunner.javamethod.JUnitJavaMethodTest;

public class AnnotationTestRuleFactory implements TestRuleFactory {

    @Override
    public Statement addTestParts(JUnitTest test, Object testObject, Statement statement) {

        Test annotation = test.getAnnotation(Test.class);
        statement = withTimeout(statement, annotation);
        statement = withExpectedException(statement, annotation);
        return statement;
    }

    Statement withTimeout(Statement statement, Test annotation) {
        long timeout = 0;
        if (annotation != null) {
            timeout = annotation.timeout();
        }

        if (timeout > 0) {
            statement = new FailOnTimeout(statement, timeout);
        }
        return statement;
    }

    Statement withExpectedException(Statement statement, Test annotation) {

        Class<? extends Throwable> expectedException = annotation.expected();
        if (expectedException == None.class) {
            expectedException = null;
        }

        if (expectedException != null) {
            ExpectedException rule = ExpectedException.none();
            rule.expect(expectedException);
            statement = rule.apply(statement, null);
        }
        return statement;
    }

    @Override
    public Statement addTestInterceptors(JUnitTest test, final Object testObject, Statement statement, Description description, Class<?> testClass) {

        TestHelper testHelper = new TestHelper(testClass);

        List<FrameworkMethod> befores = testHelper.getAnnotatedMethods(Before.class);
        if (!befores.isEmpty()) {
            Collections.reverse(befores);
            for (final FrameworkMethod before : befores) {
                statement = new BeforeMethodStatement(statement, before, testObject);
            }
        }

        List<FrameworkMethod> afters = testHelper.getAnnotatedMethods(After.class);
        for (final FrameworkMethod after : afters) {
            statement = new AfterMethodStatement(testObject, after, statement);
        }

        List<TestRule> testRules = testHelper.getAnnotatedMethodValues(testObject, Rule.class, TestRule.class);
        testRules.addAll(testHelper.getAnnotatedFieldValues(testObject, Rule.class, TestRule.class));

        FrameworkMethod frameworkMethod = new FrameworkMethod(((JUnitJavaMethodTest) test).getMethod());
        List<MethodRule> methodRules = testHelper.getAnnotatedFieldValues(testObject, Rule.class, org.junit.rules.MethodRule.class);
        methodRules.removeAll(testRules);
        for (final MethodRule each : methodRules) {
            statement = each.apply(statement, frameworkMethod, testObject);
        }

        for (TestRule rule : testRules) {
            statement = rule.apply(statement, description);
        }

        return statement;
    }

    public void validateTestRules(Class<?> testClass, List<Throwable> errors) {

        TestHelper testHelper = new TestHelper(testClass);
        testHelper.validatePublicVoidNoArgMethods(After.class, false, errors);
        testHelper.validatePublicVoidNoArgMethods(Before.class, false, errors);
        RULE_VALIDATOR.validate(testHelper.getTc(), errors);
        RULE_METHOD_VALIDATOR.validate(testHelper.getTc(), errors);
    }
}
