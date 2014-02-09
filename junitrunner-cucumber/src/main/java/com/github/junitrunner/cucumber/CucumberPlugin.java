package com.github.junitrunner.cucumber;

import gherkin.formatter.Formatter;
import gherkin.formatter.Reporter;

import java.util.ArrayList;
import java.util.List;

import org.junit.runner.notification.RunNotifier;

import com.github.junitrunner.JUnitBasePlugin;
import com.github.junitrunner.JUnitRunner;
import com.github.junitrunner.JUnitTask;
import com.github.junitrunner.StepRunnerListenerDelegate;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber.Options;
import cucumber.runtime.ClassFinder;
import cucumber.runtime.Runtime;
import cucumber.runtime.RuntimeOptions;
import cucumber.runtime.RuntimeOptionsFactory;
import cucumber.runtime.io.MultiLoader;
import cucumber.runtime.io.ResourceLoader;
import cucumber.runtime.io.ResourceLoaderClassFinder;
import cucumber.runtime.junit.Assertions;
import cucumber.runtime.model.CucumberFeature;

public class CucumberPlugin extends JUnitBasePlugin {

    private final Class<?> testClass;

    Reporter reporter;
    Formatter formatter;
    boolean strict;

    private Runtime runtime;
    private List<CucumberFeature> cucumberFeatures;

    // TODO remove notifier
    RunNotifier notifier;

    StepRunnerListenerDelegate stepRunnerListenerDelegate;

    public CucumberPlugin(Class<?> testClass) {
        this.testClass = testClass;
    }

    public void plug(JUnitRunner jUnitRunner) {

        ClassLoader classLoader = testClass.getClassLoader();
        Assertions.assertNoCucumberAnnotatedMethods(testClass);

        RuntimeOptionsFactory runtimeOptionsFactory = new RuntimeOptionsFactory(testClass, new Class[] { CucumberOptions.class, Options.class });
        RuntimeOptions runtimeOptions = runtimeOptionsFactory.create();

        ResourceLoader resourceLoader = new MultiLoader(classLoader);
        ClassFinder classFinder = new ResourceLoaderClassFinder(resourceLoader, classLoader);
        runtime = new Runtime(resourceLoader, classFinder, classLoader, runtimeOptions);

        reporter = runtimeOptions.reporter(classLoader);
        formatter = runtimeOptions.formatter(classLoader);
        strict = runtimeOptions.isStrict();

        jUnitRunner.registerListener(new CucumberRunnerListener(formatter, runtime));

        cucumberFeatures = runtimeOptions.cucumberFeatures(resourceLoader);

        stepRunnerListenerDelegate = jUnitRunner.getStepRunnerListenerDelegate();
    }

    public List<JUnitTask> createTasks(List<Throwable> errors) {

        List<JUnitTask> result = new ArrayList<JUnitTask>();
        for (CucumberFeature cucumberFeature : cucumberFeatures) {
            result.add(new CucumberFeatureSuite(testClass, this, cucumberFeature, runtime));
        }
        return result;
    }
}
