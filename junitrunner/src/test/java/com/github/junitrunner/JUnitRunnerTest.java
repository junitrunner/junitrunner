package com.github.junitrunner;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Ignore;
import org.junit.Test;

public class JUnitRunnerTest {

    @Test
    public void shouldObeyIgnoreAnnotationOnAClass() throws Exception {

        MockRunNotifier notifier = new MockRunNotifier();

        JUnitRunner unit = new JUnitRunner(IgnoredTestClass.class);

        unit.run(notifier);

        assertThat(notifier.getCalls(), is(Arrays.asList("fireTestIgnored")));
    }

    @Ignore
    public static class IgnoredTestClass {

        @Test
        public void shouldNotBeInvoked() {
            fail();
        }
    }
}
