package steps;

import api.RestAssuredUtil;
import base.DriverManager;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
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
import utils.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ModelNSteps {
    private final WebDriver driver;
    public static Logger log = LogManager.getLogger(LoginPage.class);
    private ModelNPage modelNPage;
    private ModelNLoginPage modelNLoginPage;
    private ModelNDashboardPage modelNDashboardPage;
    private WebDriverUtils utils;
    public ModelNSteps() {
        this.driver = DriverManager.getDriver();
        modelNPage = new ModelNPage(driver);
        modelNLoginPage = new ModelNLoginPage(driver);
        modelNDashboardPage = new ModelNDashboardPage(driver);
        utils=new WebDriverUtils(driver);
    }


    @Given("I open the browser and navigate to ModelN Application")
    public void iOpenTheBrowserAndNavigateToModelNApplication() {
        driver.get("https://mnipdnxlsappvm426.modeln.com/modeln/app/launchPage.html?autoLaunch=true");
        log.info("Application is launched");
        // Ensure the page is fully loaded using JavaScriptExecutor
        utils.waitForPageToLoad();
        modelNPage.clickLaunchButton();
    }

    @And("I clicked on launch ModelN button")
    public void iClickedOnLaunchModelNButton() {

        utils.waitForPageToLoad();
        utils.switchToLastWindow();
    }

    @And("I login to the application ModelN")
    public void iLoginToTheApplicationModelN() {
        modelNLoginPage.enterUserName("sadm");
        modelNLoginPage.enterPassword("sadm");
        modelNLoginPage.clickLoginButton();

    }

    @And("User navigate to the customers menu")
    public void userNavigateToTheCustomersMenu() {
        modelNDashboardPage.clickMainMenu().clickViewMenu().clickCustomersMenu().clickAccountsMenu();
        utils.switchToFrameByID("documentIFrame");
        modelNDashboardPage.selectAccountsMenu("Active");
        modelNDashboardPage.selectCustomerValue("GPO");
        modelNDashboardPage.selectDomainValue("Commercial");
        modelNDashboardPage.selectContractsValue("Yes");
        modelNDashboardPage.clickSaveAs();
    }
}
