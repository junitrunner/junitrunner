package com.github.junitrunner.cucumber;

import static cucumber.runtime.Runtime.*;
import gherkin.formatter.Formatter;
import gherkin.formatter.Reporter;
import gherkin.formatter.model.Match;
import gherkin.formatter.model.Result;
import gherkin.formatter.model.Step;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.runner.Description;

import com.github.junitrunner.JUnitTest;
import com.github.junitrunner.StepRunnerListenerDelegate;

import cucumber.api.PendingException;
import cucumber.runtime.Runtime;
import cucumber.runtime.model.CucumberBackground;
import cucumber.runtime.model.CucumberScenario;

public class CucumberScenarioTest extends JUnitTest {

    private final CucumberPlugin cucumberPlugin;

    private final Runtime runtime;
    private final CucumberScenario cucumberScenario;
    private final CucumberBackground cucumberBackground;
    private final List<Step> backgroundSteps;
    private final Map<Step, CucumberStep> stepsMap = new HashMap<Step, CucumberStep>();

    private final List<CucumberStep> steps = new ArrayList<CucumberStep>();
    private final List<CucumberStep> stepsQueue = new ArrayList<CucumberStep>();

    private String scenarioName;
    private StepRunnerListenerDelegate stepRunnerListenerContainer;
    private Throwable stepFailure = null;

    public CucumberScenarioTest(Runtime runtime, CucumberScenario scenario, CucumberPlugin cucumberPlugin) {
        this.cucumberPlugin = cucumberPlugin;

        this.runtime = runtime;
        this.cucumberScenario = scenario;
        this.cucumberBackground = scenario.getCucumberBackground();
        stepRunnerListenerContainer = cucumberPlugin.stepRunnerListenerDelegate;
        if (cucumberBackground != null) {
            backgroundSteps = cucumberBackground.getSteps();
            for (Step step : backgroundSteps) {
                registerStep(step);
            }
        } else {
            backgroundSteps = new ArrayList<Step>(0);
        }
        for (Step step : cucumberScenario.getSteps()) {
            registerStep(step);
        }
        scenarioName = cucumberScenario.getVisualName();
    }

    private void registerStep(Step step) {
        CucumberStep cucumberStep = new CucumberStep(step, scenarioName, cucumberPlugin.strict, stepRunnerListenerContainer);
        steps.add(cucumberStep);
        stepsMap.put(step, cucumberStep);
    }

    @Override
    public Description createDescription() {
        Description description = Description.createSuiteDescription(scenarioName, cucumberScenario.getGherkinModel());

        for (CucumberStep step : steps) {
            description.addChild(step.describe());
        }
        return description;
    }

    @Override
    protected void invoke(Object testObject) throws Throwable {

        Formatter formatter = new FormatterDelegate(cucumberPlugin.formatter) {

            @Override
            public void step(Step step) {
                stepsQueue.add(stepsMap.get(step));
                super.step(step);
            }
        };

        Reporter reporter = new ReporterDelegate(cucumberPlugin.reporter) {

            public void result(Result result) {
                CucumberStep currentStep = stepsQueue.remove(0);
                currentStep.stepRun(result);
                stepRun(result);
                super.result(result);
            }

            public void before(Match match, Result result) {
                handleHook(result);
                super.before(match, result);
            }

            public void after(Match match, Result result) {
                handleHook(result);
                super.after(match, result);
            }
        };

        cucumberScenario.run(formatter, reporter, runtime);

        if (stepFailure != null) {
            throw stepFailure;
        }
    }

    private void stepRun(Result result) {

        Throwable error = getFailure(result);
        if (error != null) {
            stepFailure = error;
        }
    }

    private Throwable getFailure(Result result) {
        Throwable error = result.getError();
        if (Result.SKIPPED == result) {
        } else if (Result.UNDEFINED == result || isPending(error)) {
            if (CucumberScenarioTest.this.cucumberPlugin.strict) {
                if (error == null) {
                    error = new PendingException();
                }
            }
        }
        return error;
    }

    private void handleHook(Result result) {
        if (result.getStatus().equals(Result.FAILED)) {
            EachTestNotifier executionUnitNotifier = new EachTestNotifier(cucumberPlugin.notifier, describe());
            executionUnitNotifier.addFailure(result.getError());
        }
    }
}
