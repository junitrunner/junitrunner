package com.github.junitrunner.plugin.annotation;

import static org.junit.internal.runners.rules.RuleFieldValidator.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import com.github.junitrunner.TestHelper;
import com.github.junitrunner.javamethod.AfterMethodStatement;
import com.github.junitrunner.javamethod.BeforeMethodStatement;

public class AnnotationClassRuleFactory implements ClassRuleFactory {

    @Override
    public List<TestRule> createRules(Class<?> testClass, List<Throwable> errors) {

        TestHelper testHelper = new TestHelper(testClass);
        testHelper.validatePublicVoidNoArgMethods(BeforeClass.class, true, errors);
        testHelper.validatePublicVoidNoArgMethods(AfterClass.class, true, errors);
        CLASS_RULE_VALIDATOR.validate(testHelper.getTc(), errors);
        CLASS_RULE_METHOD_VALIDATOR.validate(testHelper.getTc(), errors);

        List<TestRule> classRules = new ArrayList<TestRule>();

        for (TestRule testRule : withClassBefores(testHelper)) {
            classRules.add(testRule);
        }
        for (TestRule testRule : withClassAfters(testHelper)) {
            classRules.add(testRule);
        }
        for (TestRule testRule : classRules(testHelper)) {
            classRules.add(testRule);
        }

        return classRules;
    }

    List<TestRule> withClassBefores(TestHelper testHelper) {

        List<TestRule> classRules = new ArrayList<TestRule>();
        List<FrameworkMethod> befores = testHelper.getAnnotatedMethods(BeforeClass.class);
        for (final FrameworkMethod each : befores) {
            classRules.add(0, new TestRule() {

                @Override
                public Statement apply(Statement base, Description description) {
                    return new BeforeMethodStatement(null, each, base);
                }
            });
        }
        return classRules;
    }

    List<TestRule> withClassAfters(TestHelper testHelper) {

        List<TestRule> classRules = new ArrayList<TestRule>();
        List<FrameworkMethod> afters = testHelper.getAnnotatedMethods(AfterClass.class);
        for (final FrameworkMethod each : afters) {
            classRules.add(new TestRule() {

                @Override
                public Statement apply(Statement next, Description description) {
                    return new AfterMethodStatement(null, each, next);
                }
            });
        }
        return classRules;
    }

    List<TestRule> classRules(TestHelper testHelper) {

        List<TestRule> result = testHelper.getAnnotatedMethodValues(null, ClassRule.class, TestRule.class);
        result.addAll(testHelper.getAnnotatedFieldValues(null, ClassRule.class, TestRule.class));
        return result;
    }
}
