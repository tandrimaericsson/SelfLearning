package org.example.stepDefinitions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.example.utilities.ApiTestContext;
import org.example.utilities.PropertiesFileReader;
import org.junit.Assert;
import org.testng.asserts.SoftAssert;

import java.util.Map;
import java.util.Properties;

import static io.restassured.RestAssured.given;

public class OMDBApiSteps {

    private final ApiTestContext apiTestContext;

    public OMDBApiSteps(ApiTestContext apiTestContext) {
        this.apiTestContext = apiTestContext;
    }

    @Given("Base url is {string}")
    public void baseUrlIs(String baseUrl) {
        RestAssured.baseURI = baseUrl;
        RequestSpecification requestSpecification = given().contentType(ContentType.JSON);
        apiTestContext.setRequestSpecification(requestSpecification);
    }

    @And("Add query parameter key and value {string} {string}")
    public void addQueryParameterKeyAndValue(String key, String value) {
        if (value.startsWith("<") && value.endsWith(">")) {
            Properties properties = PropertiesFileReader.readPropertiesFile("src/test/resources/data.properties");
            if (properties.containsKey(value.replace(">", "").replace("<", "")))
                value = properties.getProperty(value.replace(">", "").replace("<", ""));
        }

        apiTestContext.setRequestSpecification(apiTestContext.getRequestSpecification().queryParam(key, value));
    }

    @When("Execute {string} resource with {string} method")
    public void executeResourceWithMethod(String resource, String httpMethod) {
        RequestSpecification requestSpecification = apiTestContext.getRequestSpecification();
        Response response = null;
        try {
            switch (httpMethod) {
                case "get":
                    response = requestSpecification.when().get(resource).then().log().all().extract().response();
                    break;
                case "post":
                    response = requestSpecification.when().post(resource).then().extract().response();
                    break;
                case "put":
                    response = requestSpecification.when().put(resource).then().extract().response();
                    break;
                case "delete":
                    response = requestSpecification.when().delete(resource).then().extract().response();
                    break;
                case "patch":
                    response = requestSpecification.when().patch(resource).then().extract().response();
                    break;
                default:
                    System.out.println("Invalid http method");
            }
        } catch (NullPointerException e) {
            System.out.println("Response is null. Message : ".concat(e.toString()));
            response = null;
        }
        apiTestContext.setResponse(response);
    }

    @Then("Assert json response using json path")
    public void assertJsonResponseUsingJsonPath(DataTable dataTable) {
        Assert.assertNotNull(apiTestContext.getResponse());
        Map<String, String> expectedValues = dataTable.asMap();
        SoftAssert softAssert = new SoftAssert();
        expectedValues.entrySet().forEach(entry -> {
            softAssert.assertEquals(apiTestContext.getResponse().jsonPath().getString(entry.getKey()), entry.getValue());
        });
        softAssert.assertAll();

    }

    @And("Assert status code is {int}")
    public void assertStatusCodeIs(int statusCode) {
        Assert.assertNotNull(apiTestContext.getResponse());
        Assert.assertEquals(apiTestContext.getResponse().getStatusCode(), statusCode);
    }

    @Then("Assert xml response attribute using xpath")
    public void assertXmlResponseAttributeUsingXpath(DataTable dataTable) {
        Map<String, String> expectedValues = dataTable.asMap();
        XmlPath xmlPath = apiTestContext.getResponse().xmlPath();
        SoftAssert softAssert = new SoftAssert();
        expectedValues.entrySet().forEach(entry -> {
            softAssert.assertEquals(xmlPath.getString(entry.getKey()), entry.getValue());
        });
        softAssert.assertAll();
    }

    @And("Assert all the movie {string} in response contains {string}")
    public void assertAllTheMovieInResponseContains(String attribute, String searchValue) {
        SoftAssert softAssert = new SoftAssert();
        apiTestContext.getResponse().jsonPath().getList("Search.".concat(attribute)).forEach(title -> softAssert.assertTrue(title.toString().contains(searchValue)));
        softAssert.assertAll();
    }

    @And("Assert all the movie {string} in xml response contains {string}")
    public void assertAllTheMovieInXmlResponseContains(String xpath, String searchValue) {
        XmlPath xmlPath = apiTestContext.getResponse().xmlPath();
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(xmlPath.getList(xpath).contains(searchValue));
        softAssert.assertAll();
    }
}
