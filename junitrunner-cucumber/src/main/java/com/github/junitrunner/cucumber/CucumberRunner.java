package com.github.junitrunner.cucumber;

import java.util.List;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

import com.github.junitrunner.JUnitPlugin;
import com.github.junitrunner.JUnitRunner;

public class CucumberRunner extends JUnitRunner {

    private CucumberPlugin cucumberPlugin;

    public CucumberRunner(Class<?> clazz) throws InitializationError {
        super(clazz);
    }

    @Override
    protected List<JUnitPlugin> discoverPlugins() throws InitializationError {
        List<JUnitPlugin> plugins = super.discoverPlugins();
        cucumberPlugin = new CucumberPlugin(testClass);
        plugins.add(cucumberPlugin);
        return plugins;
    }

    // TODO remove
    @Override
    public void run(RunNotifier notifier) {
        cucumberPlugin.notifier = notifier;
        super.run(notifier);
    }
}
