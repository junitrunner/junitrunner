package org.junitrunner.javamethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.junit.internal.runners.model.ReflectiveCallable;
import org.junit.runner.Description;
import org.junitrunner.JUnitTest;

public class JUnitJavaMethodTest extends JUnitTest {

    private final Method method;
    private Description description;

    public JUnitJavaMethodTest(Class<?> testClass, Method method) {
        this.method = method;

        description = Description.createTestDescription(testClass, method.getName(), method.getAnnotations());
    }

    @Override
    public Description describe() {
        return description;
    }

    public Method getMethod() {
        return method;
    }

    @Override
    public void invoke(final Object testObject) throws Throwable {

        new ReflectiveCallable() {
            @Override
            protected Object runReflectiveCall() throws Throwable {
                return method.invoke(testObject);
            }
        }.run();
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> cls) {
        return method.getAnnotation(cls);
    }
}
