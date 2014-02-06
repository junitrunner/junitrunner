package com.github.junitrunner.mockito;

import java.util.List;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.mockito.internal.runners.util.FrameworkUsageValidator;

import com.github.junitrunner.JUnitPlugin;
import com.github.junitrunner.JUnitRunner;

public class MockitoRunner extends JUnitRunner {

    public MockitoRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected List<JUnitPlugin> discoverPlugins() throws InitializationError {
        List<JUnitPlugin> plugins = super.discoverPlugins();
        plugins.add(new MockitoPlugin(testClass));
        return plugins;
    }

    @Override
    public void run(final RunNotifier notifier) {
        // add listener that validates framework usage at the end of each test
        notifier.addListener(new FrameworkUsageValidator(notifier));

        super.run(notifier);
    }
}
