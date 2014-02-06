package com.github.junitrunner;

import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;
import org.junit.runners.model.Statement;

public abstract class JUnitTask {

    private boolean ignored;
    private boolean filteredOut;

    protected JUnitTask() {
    }

    public boolean isIgnored() {
        return ignored;
    }

    public void ignore() {
        ignored = true;
    }

    public boolean isFilteredOut() {
        return filteredOut;
    }

    protected void filterOut() {
        filteredOut = true;
    }

    public abstract Description describe();

    protected abstract Statement constructInvokeStatement(JUnitRunner jUnitRunner);

    protected void filterTask(Filter filter) {

        Description description = describe();
        if (!filter.shouldRun(description)) {
            filteredOut = true;
        }
    }

    @SuppressWarnings("unused")
    protected void configureTask(Class<?> testClass, JUnitPlugin plugin, List<Throwable> errors) {
    }
}
