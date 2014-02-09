package com.github.junitrunner.cucumber;

import gherkin.formatter.Formatter;

import org.junit.runner.Description;

import com.github.junitrunner.Task;
import com.github.junitrunner.listener.BaseRunnerListener;

import cucumber.runtime.Runtime;
import cucumber.runtime.snippets.SummaryPrinter;

public class CucumberRunnerListener extends BaseRunnerListener {

    private final Formatter formatter;
    private final Runtime runtime;

    public CucumberRunnerListener(Formatter formatter, Runtime runtime) {
        this.formatter = formatter;
        this.runtime = runtime;
    }

    @Override
    public void testClassFinished(Description description) {
        formatter.done();
        formatter.close();
        new SummaryPrinter(System.out).print(runtime);
    }

    @Override
    public void taskStarted(Task task) {
        if (task instanceof CucumberFeatureSuite) {
            CucumberFeatureSuite feature = (CucumberFeatureSuite) task;
            formatter.uri(feature.cucumberFeature.getUri());
            formatter.feature(feature.cucumberFeature.getGherkinFeature());
        }
    }

    @Override
    public void taskFinished(Task task, Throwable failure) {
        if (task instanceof CucumberFeatureSuite) {
            formatter.eof();
        }
    }
}
