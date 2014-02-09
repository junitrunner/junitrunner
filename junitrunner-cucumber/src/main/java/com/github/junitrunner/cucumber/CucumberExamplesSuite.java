package com.github.junitrunner.cucumber;

import java.util.List;

import org.junit.runner.Description;

import com.github.junitrunner.JUnitSuite;

import cucumber.runtime.Runtime;
import cucumber.runtime.model.CucumberExamples;
import cucumber.runtime.model.CucumberScenario;

public class CucumberExamplesSuite extends JUnitSuite {

    private final CucumberExamples cucumberExamples;

    public CucumberExamplesSuite(Class<?> testClass, Runtime runtime, CucumberExamples cucumberExamples, CucumberPlugin cucumberPlugin) {
        super(testClass);
        this.cucumberExamples = cucumberExamples;

        List<CucumberScenario> exampleScenarios = cucumberExamples.createExampleScenarios();
        for (CucumberScenario scenario : exampleScenarios) {
            super.addTask(new CucumberScenarioTest(runtime, scenario, cucumberPlugin));
        }
    }

    @Override
    protected Description describeSelf() {
        return Description.createSuiteDescription(cucumberExamples.getExamples().getKeyword() + ": " + cucumberExamples.getExamples().getName(),
                cucumberExamples.getExamples());
    }
}
