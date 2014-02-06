package com.github.junitrunner.mockito;

import static org.junit.Assert.*;

import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

@RunWith(MockitoRunner.class)
public class Mockito_Runner_Test {

    @Mock
    private List<String> list;

    @Test
    public void test() {

        Mockito.when(list.get(0)).thenReturn("Here");
        assertThat(list.get(0), CoreMatchers.is("Here"));
    }
}
