package com.michaelszymczak.livingdocumentation;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:cucumber.xml"})
public class AppScenario {
    @Given("^I am on the welcome page$")
    public void i_am_on_the_welcome_page() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^I can see some text$")
    public void i_can_see_some_text() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }
}