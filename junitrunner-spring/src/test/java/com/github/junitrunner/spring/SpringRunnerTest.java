package com.github.junitrunner.spring;

import static org.hamcrest.CoreMatchers.*;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.annotation.IfProfileValue;

public class SpringRunnerTest {

    @Test
    public void shouldProcessIfProfileValue() throws Exception {

        System.setProperty("b", "y");
        SpringRunner unit = new SpringRunner(A.class);
        MockRunNotifier notifier = new MockRunNotifier();
        unit.run(notifier);

        Assert.assertThat(notifier.getCalls(), is(Arrays.asList("fireTestIgnored")));
    }

    @IfProfileValue(name = "b", value="x")
    public static class A {

        @Test
        public void x() {
        }
    }
}
