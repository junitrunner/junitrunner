package com.github.junitrunner.spring;

import java.util.List;

import org.junit.runners.model.InitializationError;

import com.github.junitrunner.JUnitPlugin;
import com.github.junitrunner.JUnitRunner;

public class SpringRunner extends JUnitRunner {

    public SpringRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    @Override
    protected List<JUnitPlugin> discoverPlugins() throws InitializationError {

        List<JUnitPlugin> plugins = super.discoverPlugins();
        plugins.add(new SpringPlugin(testClass));
        return plugins;
    }
}
