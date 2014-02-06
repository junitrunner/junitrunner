package com.github.junitrunner;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.junit.runners.model.InitializationError;

public class TestObjectFactory {

    private Constructor<?> constructor;

    public TestObjectFactory(Class<?> testClass) throws InitializationError {

        if (isANonStaticInnerClass(testClass)) {
            throw new InitializationError("The inner class " + testClass.getName() + " is not static.");
        }

        Constructor<?>[] constructors = testClass.getDeclaredConstructors();
        if (constructors.length > 1) {
            throw new InitializationError("Test class should have exactly one public constructor");
        }

        if (constructors[0].getParameterTypes().length > 0) {
            throw new InitializationError("Test class should have exactly one public zero-argument constructor");
        }

        constructor = constructors[0];
    }

    boolean isANonStaticInnerClass(Class<?> testClass) {
        return testClass.isMemberClass() && !Modifier.isStatic(testClass.getModifiers());
    }

    public Object constructTestObject() throws Throwable {
        try {
            return constructor.newInstance();
        } catch (InvocationTargetException exc) {
            throw exc.getTargetException();
        }
    }
}
