package org.junitrunner.unitils;

import java.lang.reflect.Method;

import org.junitrunner.JUnitTask;
import org.junitrunner.javamethod.JUnitJavaMethodTest;
import org.junitrunner.listener.BaseRunnerListener;
import org.unitils.core.TestListener;
import org.unitils.core.Unitils;

public class UnitilsRunnerListener extends BaseRunnerListener {

    private TestListener testListener = Unitils.getInstance().getTestListener();

    @Override
    public void testCoreStarted(JUnitTask test, Object testObject) {
        if (test instanceof JUnitJavaMethodTest) {
            Method method = ((JUnitJavaMethodTest) test).getMethod();
            testListener.beforeTestMethod(testObject, method);
        }
    }

    @Override
    public void testCoreFinished(JUnitTask test, Object testObject, Throwable error) {
        if (test instanceof JUnitJavaMethodTest) {
            Method method = ((JUnitJavaMethodTest) test).getMethod();
            testListener.afterTestMethod(testObject, method, error);
        }
    }
}
