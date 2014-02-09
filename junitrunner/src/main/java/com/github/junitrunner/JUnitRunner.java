package com.github.junitrunner;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.junit.internal.runners.statements.Fail;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.Filterable;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.manipulation.Sortable;
import org.junit.runner.manipulation.Sorter;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runner.notification.StoppedByUserException;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import com.github.junitrunner.listener.RunnerListener;
import com.github.junitrunner.listener.RunnerListenerContainer;
import com.github.junitrunner.listener.TestInvocationListenerStatement;
import com.github.junitrunner.plugin.annotation.AnnotationPlugin;

public class JUnitRunner extends Runner implements Filterable, Sortable {

    protected final Class<?> testClass;

    private List<JUnitPlugin> plugins;
    private JUnitSuite suite;
    private TestObjectFactory testObjectFactory;
    final RunnerListenerContainer listenerContainer = new RunnerListenerContainer();
    private Statement classStatement;

    public JUnitRunner(Class<?> testClass) throws InitializationError {

        this.testClass = testClass;

        suite = new JUnitSuite(testClass);

        List<Throwable> errors = new ArrayList<Throwable>();

        processPlugins();

        testObjectFactory = new TestObjectFactory(testClass);

        computeClassStatement(errors);
        validateTestRules(errors);
        // TODO: would be nice to discover tests before computeClassStatement
        discoverTests(errors);
        configureTestSuite(errors);

        if (suite.isEmpty()) {
            errors.add(new Exception("No runnable methods"));
        }

        if (!errors.isEmpty()) {
            throw new InitializationError(errors);
        }
    }

    public JUnitSuite getSuite() {
        return suite;
    }

    private void processPlugins() throws InitializationError {

        plugins = discoverPlugins();
        for (JUnitPlugin plugin : plugins) {
            plugin.plug(this);
        }
    }

    protected List<JUnitPlugin> discoverPlugins() throws InitializationError {

        WithPlugins pluginsAnnotation = testClass.getAnnotation(WithPlugins.class);

        List<JUnitPlugin> result = new ArrayList<JUnitPlugin>();

        if (pluginsAnnotation != null) {
            List<Class<? extends JUnitPlugin>> pluginClasses = getPluginClasses(pluginsAnnotation);
            for (Class<? extends JUnitPlugin> cls : pluginClasses) {
                result.add(AnnotationHelper.createObject(cls, testClass));
            }
        } else {
            result.add(AnnotationHelper.createObject(AnnotationPlugin.class, testClass));
        }

        return result;
    }

    List<Class<? extends JUnitPlugin>> getPluginClasses(WithPlugins pluginsAnnotation) throws InitializationError {

        Class<? extends JUnitPlugin>[] pluginClasses = pluginsAnnotation.value();
        if (pluginClasses == null || pluginClasses.length == 0) {
            throw new InitializationError(WithPlugins.class.getSimpleName() + " annotation specifies no plugins");
        }

        List<Class<? extends JUnitPlugin>> result = new ArrayList<Class<? extends JUnitPlugin>>();

        if (!pluginsAnnotation.skipDefault()) {
            result.add(AnnotationPlugin.class);
        }

        for (Class<? extends JUnitPlugin> cls : pluginClasses) {
            if (result.contains(cls)) {
                throw new InitializationError("Plugin type " + cls + " registered twice");
            }
            result.add(cls);
        }
        return result;
    }

    private void computeClassStatement(List<Throwable> errors) {

        /* children are not registered yet, calling getDescription() will cache incomplete description */
        Description description = suite.describeSelf();

        Statement statement = suite.constructInvokeStatement(this);

        for (JUnitPlugin plugin : plugins) {
            statement = plugin.enhanceClassStatement(statement, description, errors);
        }

        classStatement = statement;
    }

    private void configureTestSuite(List<Throwable> errors) {

        for (JUnitPlugin plugin : plugins) {
            suite.configureTask(testClass, plugin, errors);
        }
    }

    private void discoverTests(List<Throwable> errors) {

        for (JUnitPlugin plugin : plugins) {
            List<JUnitTask> tasks = plugin.createTasks(errors);
            if (tasks != null) {
                for (JUnitTask task : tasks) {
                    suite.addTask(task);
                }
            }
        }
    }

    private void validateTestRules(List<Throwable> errors) {

        for (JUnitPlugin plugin : plugins) {
            plugin.validateTestRules(errors);
        }
    }

    public void registerListener(RunnerListener listener) {
        listenerContainer.addListener(listener);
    }

    public void registerListeners(List<RunnerListener> listeners) {
        for (RunnerListener listener : listeners) {
            listenerContainer.addListener(listener);
        }
    }

    @Override
    public void run(final RunNotifier notifier) {

        registerListener(new RunNotifierWrapper(notifier));

        if (suite.isIgnored()) {
            listenerContainer.taskIgnored(suite);
            return;
        }

        Description description = getDescription();
        listenerContainer.testClassStarted(description);

        try {

            classStatement.evaluate();
        } catch (StoppedByUserException e) {
            throw e;
        } catch (Throwable e) {
            notifier.fireTestFailure(new Failure(description, e));
        } finally {
            listenerContainer.testClassFinished(description);
        }
    }

    protected Statement buildTestParts(final JUnitTest test, Object testObject, Statement statement) {

        for (JUnitPlugin plugin : plugins) {
            statement = plugin.enhanceTestCore(test, testObject, statement);
        }
        return statement;
    }

    public Statement buildTestInteceptors(JUnitTest test, final Object testObject, Statement statement) {
        for (JUnitPlugin plugin : plugins) {
            statement = plugin.enhanceTest(test, testObject, statement);
        }
        return statement;
    }

    @Override
    public Description getDescription() {
        return suite.describe();
    }

    public void filter(Filter filter) throws NoTestsRemainException {

        suite.filterTask(filter);
        if (suite.isFilteredOut()) {
            throw new NoTestsRemainException();
        }
    }

    public void sort(final Sorter sorter) {

        Comparator<JUnitTask> descriptionComparator = new Comparator<JUnitTask>() {
            public int compare(JUnitTask o1, JUnitTask o2) {
                return sorter.compare(o1.describe(), o2.describe());
            }
        };

        suite.sortSuite(descriptionComparator);
    }

    protected final Statement constructTestStatement(JUnitTest test) {

        Statement statement;
        try {
            Object testObject = testObjectFactory.constructTestObject();
            statement = test.constructExecuteStatement(testObject);
            statement = buildTestParts(test, testObject, statement);
            statement = new TestInvocationListenerStatement(test, statement, testObject, listenerContainer);
            statement = buildTestInteceptors(test, testObject, statement);
        } catch (Throwable e) {
            statement = new Fail(e);
        }
        return statement;
    }

    protected Statement constructSuiteStatement(final JUnitSuite suite) {

        return new Statement() {

            @Override
            public void evaluate() throws Throwable {
                suite.invoke(JUnitRunner.this);
            }
        };
    }

    public StepRunnerListenerDelegate getStepRunnerListenerDelegate() {
        return new StepRunnerListenerDelegate(listenerContainer);
    }
}
