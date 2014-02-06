package com.github.junitrunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;
import org.junit.runners.model.Statement;

public class JUnitSuite extends JUnitTask {

    private final Class<?> testClass;
    private List<JUnitTask> children = new ArrayList<JUnitTask>();

    public JUnitSuite(Class<?> testClass) {
        this.testClass = testClass;
    }

    void addTask(JUnitTask task) {
        children.add(task);
    }

    public boolean isEmpty() {
        return children.isEmpty();
    }

    @Override
    public Description describe() {

        Description description = Description.createSuiteDescription(testClass.getName(), testClass.getAnnotations());
        for (JUnitTask test : children) {
            if (test.isFilteredOut()) {
                continue;
            }
            description.addChild(test.describe());
        }
        return description;
    }

    protected void invoke(JUnitRunner jUnitRunner) throws Throwable {

        boolean hasFailures = false;

        for (JUnitTask task : children) {
            if (task.isFilteredOut()) {
                continue;
            }
            if (task.isIgnored()) {
                jUnitRunner.listenerContainer.taskIgnored(task);
            } else {
                Statement statement = task.constructInvokeStatement(jUnitRunner);
                jUnitRunner.listenerContainer.taskStarted(task);
                try {
                    statement.evaluate();
                    jUnitRunner.listenerContainer.taskFinished(task);
                } catch (Throwable exc) {
                    jUnitRunner.listenerContainer.taskFailed(task, exc);
                    hasFailures = true;
                }
            }
        }

        if (hasFailures) {
            throw new Exception("Some tests failed");
        }
    }

    protected final Statement constructInvokeStatement(JUnitRunner jUnitRunner) {
        return jUnitRunner.constructSuiteStatement(this);
    }

    private boolean allTestsFilteredOut() {

        boolean allTestsFilteredOut = true;
        for (JUnitTask task : children) {
            if (!task.isFilteredOut()) {
                allTestsFilteredOut = false;
                break;
            }
        }
        return allTestsFilteredOut;
    }

    @Override
    protected void filterTask(Filter filter) {

        super.filterTask(filter);

        for (JUnitTask task : children) {
            if (!task.isFilteredOut()) {
                task.filterTask(filter);
            }
        }

        if (allTestsFilteredOut()) {
            filterOut();
        }
    }

    void sortSuite(Comparator<JUnitTask> comparator) {

        for (JUnitTask task : children) {
            if (task instanceof JUnitSuite) {
                JUnitSuite suite = (JUnitSuite) task;
                suite.sortSuite(comparator);
            }
        }

        Collections.sort(children, comparator);
    }

    @Override
    protected void configureTask(Class<?> testClass, JUnitPlugin plugin, List<Throwable> errors) {
        plugin.configureSuite(this, errors);
        for (JUnitTask child : children) {
            child.configureTask(testClass, plugin, errors);
        }
    }
}
