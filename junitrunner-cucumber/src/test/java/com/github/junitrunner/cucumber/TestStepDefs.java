package com.github.junitrunner.cucumber;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class TestStepDefs {

    @Given("^background$")
    public void background() throws Throwable {
        // do background
    }

    @Given("^step 1$")
    public void step1() throws Throwable {
        // Express the Regexp above with the code you wish you had
    }

    @Then("^step 2$")
    public void step2() throws Throwable {
        // Express the Regexp above with the code you wish you had
    }

    @Given("^there are (\\d+) cucumbers$")
    public void there_are_cucumbers(int arg1) throws Throwable {
        // Express the Regexp above with the code you wish you had
    }

    @When("^I eat (\\d+) cucumbers$")
    public void I_eat_cucumbers(int arg1) throws Throwable {
        // Express the Regexp above with the code you wish you had
    }

    @Then("^I should have (\\d+) cucumbers$")
    public void I_should_have_cucumbers(int arg1) throws Throwable {
        // Express the Regexp above with the code you wish you had
    }
}
