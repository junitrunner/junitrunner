package com.github.junitrunner.cucumber;

import gherkin.formatter.model.Feature;

import org.junit.runner.Description;

import com.github.junitrunner.JUnitSuite;

import cucumber.runtime.Runtime;
import cucumber.runtime.model.CucumberFeature;
import cucumber.runtime.model.CucumberScenario;
import cucumber.runtime.model.CucumberScenarioOutline;
import cucumber.runtime.model.CucumberTagStatement;

public class CucumberFeatureSuite extends JUnitSuite {

    final CucumberPlugin cucumberPlugin;

    final CucumberFeature cucumberFeature;

    public CucumberFeatureSuite(Class<?> testClass, CucumberPlugin cucumberPlugin, CucumberFeature cucumberFeature, Runtime runtime) {
        super(testClass);
        this.cucumberPlugin = cucumberPlugin;
        this.cucumberFeature = cucumberFeature;

        for (CucumberTagStatement cucumberTagStatement : cucumberFeature.getFeatureElements()) {
            if (cucumberTagStatement instanceof CucumberScenario) {
                addTask(new CucumberScenarioTest(runtime, (CucumberScenario)cucumberTagStatement, cucumberPlugin));
            } else {
                addTask(new CucumberScenarioOutlineSuite(testClass, runtime, (CucumberScenarioOutline)cucumberTagStatement, cucumberPlugin));
            }
        }
    }

    @Override
    protected Description describeSelf() {
        return Description.createSuiteDescription(getFeatureName(), cucumberFeature.getGherkinFeature());
    }

    private String getFeatureName() {
        Feature feature = cucumberFeature.getGherkinFeature();
        return feature.getKeyword() + ": " + feature.getName();
    }
}
