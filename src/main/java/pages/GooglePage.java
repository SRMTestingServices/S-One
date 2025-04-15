package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.SeleniumUtils;
import utils.WaitUtils;

public class GooglePage {
    private WebDriver driver;
    private SeleniumUtils seleniumUtils;
    private WaitUtils waitUtils;


    private By searchBox = By.id("APjFqb");
    private By passwordField = By.id("password");
    private By loginButton = By.id("loginBtn");
    private By errorMessage = By.id("errorMsg");

    public GooglePage(WebDriver driver) {
        this.driver = driver;
        seleniumUtils = new SeleniumUtils(driver);
        waitUtils = new WaitUtils(driver);
    }

    public void enterSearchBox(String searchText) {
        seleniumUtils.typeText(driver.findElement(searchBox), searchText);
    }

    public void enterPassword(String password) {
        seleniumUtils.typeText(driver.findElement(passwordField), password);
    }

    public void clickLoginButton() {
        waitUtils.waitForElementToBeClickable(driver.findElement(loginButton), 10);
        seleniumUtils.clickElement(driver.findElement(loginButton));
    }

    public String getErrorMessage() {
        return seleniumUtils.getElementText(driver.findElement(errorMessage));
    }
}
