package org.junitrunner;

import java.lang.annotation.Annotation;
import java.util.List;

import org.junit.runners.model.Statement;

public abstract class JUnitTest extends JUnitTask {

    public <T extends Annotation> T getAnnotation(@SuppressWarnings("unused") Class<T> cls) {
        return null;
    }

    protected abstract void invoke(final Object testObject) throws Throwable;

    protected final Statement constructInvokeStatement(JUnitRunner jUnitRunner) {
        return jUnitRunner.constructTestStatement(this);
    }

    public Statement constructExecuteStatement(final Object testObject) {
        return new Statement() {

            @Override
            public void evaluate() throws Throwable {
                invoke(testObject);
            }
        };
    }

    @Override
    protected void configureTask(Class<?> testClass, JUnitPlugin plugin, List<Throwable> errors) {
        plugin.configureTest(this, errors);
    }
}
