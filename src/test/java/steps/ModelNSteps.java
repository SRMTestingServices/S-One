package steps;

import api.RestAssuredUtil;
import base.DriverManager;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import pages.*;
import reporting.HtmlReportGenerator;
import reporting.Status;
import reporting.TestStep;
import utils.ScreenshotUtil;
import utils.StepNamePlugin;
import utils.WaitUtils;
import utils.WebDriverUtil;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ModelNSteps {
    private final WebDriver driver;
    public static Logger log = LogManager.getLogger(LoginPage.class);
    List<TestStep> testSteps = new ArrayList<>();

    public ModelNSteps() {
        this.driver = DriverManager.getDriver();
    }


    @Given("I open the browser and navigate to ModelN Application")
    public void iOpenTheBrowserAndNavigateToModelNApplication() {
        ModelNPage modelNPage=new ModelNPage(driver);
        driver.get("https://mnipdnxlsappvm426.modeln.com/modeln/app/launchPage.html?autoLaunch=true");
        log.info("Application is launched");
        // Ensure the page is fully loaded using JavaScriptExecutor
        WaitUtils.waitForPageToLoad(10);
        //testSteps.add(new TestStep("1", "Step 1", "Launch the modeln Application", Status.PASS, ScreenshotUtil.captureScreenshot(driver)));
        modelNPage.clickLaunchButton();
    }

    @And("I clicked on launch ModelN button")
    public void iClickedOnLaunchModelNButton() {

        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.jsReturnsValue("return document.readyState=='complete'"));
        Set<String> windowHandles = driver.getWindowHandles();
        // Convert to list and pick the last window
        String[] handlesArray = windowHandles.toArray(new String[0]);
        String lastWindowHandle = handlesArray[handlesArray.length - 1];
        // Switch to the last window
        driver.switchTo().window(lastWindowHandle);
        ModelNLoginPage modelNLoginPage=new ModelNLoginPage(driver);
        modelNLoginPage.enterUserName("sadm");
        //testSteps.add(new TestStep("2", "Step 2", "Enter the username", Status.PASS, ScreenshotUtil.captureScreenshot(driver)));
        modelNLoginPage.enterPassword("sadm");
       // testSteps.add(new TestStep("3", "Step 3", "Enter the password", Status.PASS, ScreenshotUtil.captureScreenshot(driver)));
        modelNLoginPage.clickLoginButton();
        ModelNDashboardPage modelNDashboardPage=new ModelNDashboardPage(driver);
        modelNDashboardPage.clickMainMenu().clickViewMenu().clickCustomersMenu();
        //HtmlReportGenerator.generateHtmlReport("Chrome", "ModuleN application test", "Project - ModuleN", testSteps,false);
    }
}
