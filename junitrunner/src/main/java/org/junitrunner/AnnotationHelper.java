package org.junitrunner;

import java.util.ArrayList;
import java.util.List;

public class AnnotationHelper {

    public static <T> T createObject(Class<? extends T> cls, Class<?> arg) {

        try {
            return cls.getConstructor(Class.class).newInstance(arg);
        } catch (Exception exc) {
            throw new RuntimeException(exc);
        }
    }

    static <T> T createObject(Class<? extends T> cls) {

        try {
            return cls.getConstructor().newInstance();
        } catch (Exception exc) {
            throw new RuntimeException(exc);
        }
    }

    public static <T> List<T> constructInstances(Class<? extends T>[] classes, Class<? extends T> def) {

        List<T> result = new ArrayList<T>();
        if (classes == null || classes.length == 0) {
            if (def != null) {
                result.add(createObject(def));
            }
        } else {
            for (Class<? extends T> cls : classes) {
                result.add(createObject(cls));
            }
        }
        return result;
    }
}
