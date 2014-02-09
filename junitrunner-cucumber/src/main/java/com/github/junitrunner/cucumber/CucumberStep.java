package com.github.junitrunner.cucumber;

import static cucumber.runtime.Runtime.*;
import gherkin.formatter.model.Result;
import gherkin.formatter.model.Step;

import java.io.Serializable;

import org.junit.runner.Description;

import com.github.junitrunner.Task;
import com.github.junitrunner.StepRunnerListenerDelegate;

import cucumber.api.PendingException;

public class CucumberStep extends Task implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Step step;
    private final String scenarioName;
    private final boolean strict;
    private final StepRunnerListenerDelegate stepRunnerListenerContainer;

    public CucumberStep(Step step, String scenarioName, boolean strict, StepRunnerListenerDelegate stepRunnerListenerContainer) {
        this.step = step;
        this.scenarioName = scenarioName;
        this.strict = strict;
        this.stepRunnerListenerContainer = stepRunnerListenerContainer;
    }

    @Override
    public Description createDescription() {
        return Description.createTestDescription(scenarioName, step.getKeyword() + step.getName(), this);
    }

    public void stepRun(Result result) {

        Description currentStepDescription = describe();
        Throwable error = result.getError();
        if (Result.SKIPPED == result) {
            stepRunnerListenerContainer.stepIgnored(this);
        } else if (Result.UNDEFINED == result || isPending(error)) {
            if (strict) {
                if (error == null) {
                    error = new PendingException();
                }
                stepRunnerListenerContainer.stepStarted(this);
                stepRunnerListenerContainer.stepFinished(this, error); // finish not fired
            } else {
                ignore();
                stepRunnerListenerContainer.stepIgnored(this);
            }
        } else {
            if (currentStepDescription != null) {
                stepRunnerListenerContainer.stepStarted(this);
                stepRunnerListenerContainer.stepFinished(this, error);
            }
        }
    }
}
