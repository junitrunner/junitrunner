package com.github.junitrunner.cucumber;

import org.junit.runner.Description;

import com.github.junitrunner.JUnitSuite;

import cucumber.runtime.Runtime;
import cucumber.runtime.model.CucumberExamples;
import cucumber.runtime.model.CucumberScenarioOutline;

public class CucumberScenarioOutlineSuite extends JUnitSuite {

    private final CucumberScenarioOutline cucumberScenarioOutline;

    public CucumberScenarioOutlineSuite(Class<?> testClass, Runtime runtime, CucumberScenarioOutline cucumberScenarioOutline,
            CucumberPlugin cucumberPlugin) {
        super(testClass);
        this.cucumberScenarioOutline = cucumberScenarioOutline;

        for (CucumberExamples cucumberExamples : cucumberScenarioOutline.getCucumberExamplesList()) {
            CucumberExamplesSuite examplesSuite = new CucumberExamplesSuite(testClass, runtime, cucumberExamples, cucumberPlugin);
            super.addTask(examplesSuite);
        }
    }

    @Override
    protected Description describeSelf() {
        return Description.createSuiteDescription(cucumberScenarioOutline.getVisualName(), cucumberScenarioOutline.getGherkinModel());
    }
}
