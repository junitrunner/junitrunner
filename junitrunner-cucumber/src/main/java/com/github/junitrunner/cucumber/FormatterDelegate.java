package com.github.junitrunner.cucumber;

import gherkin.formatter.Formatter;
import gherkin.formatter.model.Background;
import gherkin.formatter.model.Examples;
import gherkin.formatter.model.Feature;
import gherkin.formatter.model.Scenario;
import gherkin.formatter.model.ScenarioOutline;
import gherkin.formatter.model.Step;

import java.util.List;

public class FormatterDelegate implements Formatter {

    private Formatter delegate;

    public FormatterDelegate(Formatter delegate) {
        this.delegate = delegate;
    }

    public void uri(String uri) {
        delegate.uri(uri);
    }

    public void feature(Feature feature) {
        delegate.feature(feature);
    }

    public void background(Background background) {
        delegate.background(background);
    }

    public void scenario(Scenario scenario) {
        delegate.scenario(scenario);
    }

    public void scenarioOutline(ScenarioOutline scenarioOutline) {
        delegate.scenarioOutline(scenarioOutline);
    }

    public void examples(Examples examples) {
        delegate.examples(examples);
    }

    public void step(Step step) {
        delegate.step(step);
    }

    public void eof() {
        delegate.eof();
    }

    public void syntaxError(String state, String event, List<String> legalEvents, String uri, Integer line) {
        delegate.syntaxError(state, event, legalEvents, uri, line);
    }

    public void done() {
        delegate.done();
    }

    public void close() {
        delegate.close();
    }
}
