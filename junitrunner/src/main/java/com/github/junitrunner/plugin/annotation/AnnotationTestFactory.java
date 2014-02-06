package com.github.junitrunner.plugin.annotation;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

import com.github.junitrunner.JUnitTask;
import com.github.junitrunner.TestHelper;
import com.github.junitrunner.javamethod.JUnitJavaMethodTest;

public class AnnotationTestFactory implements TestFactory {

    @Override
    public List<JUnitTask> createTests(Class<?> testClass, List<Throwable> errors) {

        TestHelper testHelper = new TestHelper(testClass);
        testHelper.validatePublicVoidNoArgMethods(Test.class, false, errors);

        TestClass tc = new TestClass(testClass);
        List<FrameworkMethod> annotatedMethods = tc.getAnnotatedMethods(Test.class);

        List<JUnitTask> result = new ArrayList<JUnitTask>();
        for (FrameworkMethod fm : annotatedMethods) {
            result.add(new JUnitJavaMethodTest(testClass, fm.getMethod()));
        }
        return result;
    }
}
