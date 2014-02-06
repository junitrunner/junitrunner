package com.github.junitrunner.mockito;

import static org.junit.Assert.*;

import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.github.junitrunner.JUnitRunner;
import com.github.junitrunner.WithPlugins;

@RunWith(JUnitRunner.class)
@WithPlugins(MockitoPlugin.class)
public class Mockito_Plugin_Test {

    @Mock
    private List<String> list;

    @Test
    public void test() {

        Mockito.when(list.get(0)).thenReturn("Here");
        assertThat(list.get(0), CoreMatchers.is("Here"));
    }
}
