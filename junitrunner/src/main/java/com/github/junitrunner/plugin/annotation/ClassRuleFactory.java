package com.github.junitrunner.plugin.annotation;

import java.util.List;

import org.junit.rules.TestRule;

public interface ClassRuleFactory {

    List<TestRule> createRules(Class<?> testClass, List<Throwable> errors);
}
