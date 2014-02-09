package com.github.junitrunner.cucumber;

import gherkin.formatter.Reporter;
import gherkin.formatter.model.Match;
import gherkin.formatter.model.Result;

public class ReporterDelegate implements Reporter {

    private final Reporter delegate;

    public ReporterDelegate(Reporter delegate) {
        this.delegate = delegate;
    }

    public void before(Match match, Result result) {
        delegate.before(match, result);
    }

    public void result(Result result) {
        delegate.result(result);
    }

    public void after(Match match, Result result) {
        delegate.after(match, result);
    }

    public void match(Match match) {
        delegate.match(match);
    }

    public void embedding(String mimeType, byte[] data) {
        delegate.embedding(mimeType, data);
    }

    public void write(String text) {
        delegate.write(text);
    }
}
