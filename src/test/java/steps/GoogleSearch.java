package steps;

import base.DriverManager;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import pages.LoginPage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class GoogleSearch {
    private final WebDriver driver;
    public static Logger log = LogManager.getLogger(LoginPage.class);


    public GoogleSearch() {
        this.driver = DriverManager.getDriver();
    }


    @Given("I open the browser and navigate to {string}")
    public void iOpenTheBrowserAndNavigateTo(String arg0) {
        driver.get(arg0);
        log.info("Driver is initialized");
    }

    @Then("the page title should be {string}")
    public void thePageTitleShouldBe(String expectedTitle) {
        String actualTitle = driver.getTitle();
        Assert.assertEquals(actualTitle, expectedTitle, "Page title mismatch!");
        log.info("Page title is validated");
    }
}
