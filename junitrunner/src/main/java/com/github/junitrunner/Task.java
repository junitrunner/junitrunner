package com.github.junitrunner;

import org.junit.runner.Description;

public abstract class Task {

    protected Description description;
    private boolean ignored;

    public final Description describe() {
        if (description == null) {
            description = createDescription();
        }
        return description;
    }

    public abstract Description createDescription();

    public boolean isIgnored() {
        return ignored;
    }

    public void ignore() {
        ignored = true;
    }
}
