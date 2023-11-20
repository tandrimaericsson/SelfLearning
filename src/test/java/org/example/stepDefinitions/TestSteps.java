package org.example.stepDefinitions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.it.Ma;
import org.example.utilities.ManageWebDriver;
import org.example.utilities.UiTestContext;

import java.util.*;

public class TestSteps {

    private UiTestContext uiTestContext;
    private ManageWebDriver manageWebDriver;

    public TestSteps(UiTestContext uiTestContext){
        this.uiTestContext=uiTestContext;
        manageWebDriver=uiTestContext.getManageWebDriver();
    }

    @Before(order=1)
    public void initializeDriver(){
        manageWebDriver=new ManageWebDriver();
        manageWebDriver.initializeDriver();
    }

    @Given("the user is on the registration page")
    public void theUserIsOnTheRegistrationPage() {
        System.out.println("Navigate to the registration page");
    }

    @When("the user enters the following details and submits:")
    public void theUserEntersTheFollowingDetails(DataTable dataTable) {
        List<Map<String, String>> dataList = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> data : dataList) {
            String firstName = data.get("First Name");
            String lastName = data.get("Last Name");
            String email = data.get("Email Address");
            String password = data.get("Password");

            System.out.println(firstName);
            System.out.print(lastName);
            System.out.print(email);
            System.out.println(password);

            // Fill in the registration form with the provided data and submit
        }
    }

    @Then("the user should be registered successfully")
    public void theUserShouldBeRegisteredSuccessfully() {
        System.out.println("Verify that the registration was successful");
    }

    @Given("Open {string} website")
    public void openWebsite(String url) {
        manageWebDriver.openUrl(url);
    }

    @Then("Get page title")
    public void getPageTitle() {
        System.out.println(manageWebDriver.getDriver().getTitle());
    }

    @After
    public void quitBrowser(){
        manageWebDriver.quit();
    }
}
