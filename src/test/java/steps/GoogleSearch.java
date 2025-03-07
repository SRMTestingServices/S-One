package steps;

import base.DriverManager;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class GoogleSearch {
    private final WebDriver driver;

    public GoogleSearch() {
        this.driver = DriverManager.getDriver();
    }


    @Given("I open the browser and navigate to {string}")
    public void iOpenTheBrowserAndNavigateTo(String arg0) {
        driver.get(arg0);
    }

    @Then("the page title should be {string}")
    public void thePageTitleShouldBe(String expectedTitle) {
        String actualTitle = driver.getTitle();
        Assert.assertEquals(actualTitle, expectedTitle, "Page title mismatch!");
    }
}
