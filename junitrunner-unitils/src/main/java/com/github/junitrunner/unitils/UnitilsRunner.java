package org.junitrunner.unitils;

import java.util.List;

import org.junit.runners.model.InitializationError;
import org.junitrunner.JUnitPlugin;
import org.junitrunner.JUnitRunner;

public class UnitilsRunner extends JUnitRunner {

    public UnitilsRunner(Class<?> testClass) throws Exception {
        super(testClass);

        registerListener(new UnitilsRunnerListener());
    }

    @Override
    protected List<JUnitPlugin> discoverPlugins() throws InitializationError {
        List<JUnitPlugin> plugins = super.discoverPlugins();
        plugins.add(new UnitilsPlugin(testClass));
        return plugins;
    }
}
