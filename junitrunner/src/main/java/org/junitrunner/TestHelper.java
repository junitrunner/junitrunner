package org.junitrunner;

import java.lang.annotation.Annotation;
import java.util.List;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

public class TestHelper {

    private final Class<?> testClass;
    private final TestClass tc;

    public TestHelper(Class<?> testClass) {
        this.testClass = testClass;
        tc = new TestClass(testClass);
    }

    public Class<?> getTestClass() {
        return testClass;
    }

    @Deprecated
    public TestClass getTc() {
        return tc;
    }

    public <A extends Annotation> A getAnnotation(Class<A> annotationType) {

        return testClass.getAnnotation(annotationType);
    }

    public List<FrameworkMethod> getAnnotatedMethods(Class<? extends Annotation> annotation) {

        return tc.getAnnotatedMethods(annotation);
    }

    public Annotation[] getAnnotations() {

        return testClass.getAnnotations();
    }

    public String getName() {

        return testClass.getName();
    }

    public <T> List<T> getAnnotatedMethodValues(Object test, Class<? extends Annotation> annotationClass, Class<T> valueClass) {
        return tc.getAnnotatedMethodValues(test, annotationClass, valueClass);
    }

    public <T> List<T> getAnnotatedFieldValues(Object test, Class<? extends Annotation> annotationClass, Class<T> valueClass) {
        return tc.getAnnotatedFieldValues(test, annotationClass, valueClass);
    }

    public void validatePublicVoidNoArgMethods(Class<? extends Annotation> annotation, boolean isStatic, List<Throwable> errors) {

        List<FrameworkMethod> methods = getAnnotatedMethods(annotation);

        for (FrameworkMethod eachTestMethod : methods) {
            eachTestMethod.validatePublicVoidNoArg(isStatic, errors);
        }
    }
}
