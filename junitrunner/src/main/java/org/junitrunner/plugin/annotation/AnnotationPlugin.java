package org.junitrunner.plugin.annotation;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.junitrunner.AnnotationHelper;
import org.junitrunner.JUnitPlugin;
import org.junitrunner.JUnitRunner;
import org.junitrunner.JUnitSuite;
import org.junitrunner.JUnitTask;
import org.junitrunner.JUnitTest;
import org.junitrunner.listener.RunnerListener;

public class AnnotationPlugin implements JUnitPlugin {

    private final Class<?> testClass;
    private List<TestRuleFactory> testRuleFactories;
    private List<TestFactory> testFactories;

    public AnnotationPlugin(Class<?> testClass) {
        this.testClass = testClass;
    }

    @Override
    public void plug(JUnitRunner jUnitRunner) {

        testFactories = configureTestFactories();
        testRuleFactories = configureTestRuleFactories();
        jUnitRunner.registerListeners(configureListeners());
    }

    @Override
    public void validateTestRules(List<Throwable> errors) {
        for (TestRuleFactory testRuleFactory : testRuleFactories) {
            testRuleFactory.validateTestRules(testClass, errors);
        }
    }

    @Override
    public Statement enhanceClassStatement(Statement statement, Description description, List<Throwable> errors) {

        WithClassRuleFactories factory = testClass.getAnnotation(WithClassRuleFactories.class);
        Class<? extends ClassRuleFactory>[] ruleFactoryClasses = null;
        if (factory != null) {
            ruleFactoryClasses = factory.value();
        }
        List<ClassRuleFactory> classRuleFactories = AnnotationHelper.constructInstances(ruleFactoryClasses, AnnotationClassRuleFactory.class);

        for (ClassRuleFactory classRuleFactory : classRuleFactories) {
            List<TestRule> rules = classRuleFactory.createRules(testClass, errors);
            if (rules != null) {
                for (TestRule rule : rules) {
                    statement = rule.apply(statement, description);
                }
            }
        }

        return statement;
    }

    @Override
    public List<JUnitTask> createTasks(List<Throwable> errors) {
        List<JUnitTask> tasks = new ArrayList<JUnitTask>();
        for (TestFactory testFactory : testFactories) {
            tasks.addAll(testFactory.createTests(testClass, errors));
        }
        return tasks;
    }

    @Override
    public Statement enhanceTestCore(JUnitTest test, Object testObject, Statement statement) {
        for (TestRuleFactory testRuleFactory : testRuleFactories) {
            statement = testRuleFactory.addTestParts(test, testObject, statement);
        }
        return statement;
    }

    @Override
    public Statement enhanceTest(JUnitTest test, Object testObject, Statement statement) {
        for (TestRuleFactory testRuleFactory : testRuleFactories) {
            statement = testRuleFactory.addTestInterceptors(test, testObject, statement, test.describe(), testClass);
        }
        return statement;
    }

    @Override
    public void configureSuite(JUnitSuite suite, List<Throwable> errors) {

        Ignore annotation = testClass.getAnnotation(Ignore.class);
        if (annotation != null) {
            suite.ignore();
        }
    }

    @Override
    public void configureTest(JUnitTest test, List<Throwable> errors) {
        if (test.getAnnotation(Ignore.class) != null) {
            test.ignore();
        }
    }

    List<TestFactory> configureTestFactories() {
        WithTestFactories factory = testClass.getAnnotation(WithTestFactories.class);

        Class<? extends TestFactory>[] testFactoryClasses = null;
        if (factory != null) {
            testFactoryClasses = factory.value();
        }
        return AnnotationHelper.constructInstances(testFactoryClasses, AnnotationTestFactory.class);
    }

    List<TestRuleFactory> configureTestRuleFactories() {
        WithTestRuleFactories factory = testClass.getAnnotation(WithTestRuleFactories.class);

        Class<? extends TestRuleFactory>[] ruleFactoryClasses = null;
        if (factory != null) {
            ruleFactoryClasses = factory.value();
        }
        return AnnotationHelper.constructInstances(ruleFactoryClasses, AnnotationTestRuleFactory.class);
    }

    List<RunnerListener> configureListeners() {
        WithListeners listeners = testClass.getAnnotation(WithListeners.class);

        Class<? extends RunnerListener>[] listenerClasses = null;
        if (listeners != null) {
            listenerClasses = listeners.value();
        }
        return AnnotationHelper.constructInstances(listenerClasses, null);
    }
}
