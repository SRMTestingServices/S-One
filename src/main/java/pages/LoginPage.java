package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.SeleniumUtils;
import utils.WaitUtils;

public class LoginPage {
    private WebDriver driver;
    private SeleniumUtils seleniumUtils;
    private WaitUtils waitUtils;


    private By usernameField = By.id("username");
    private By passwordField = By.id("password");
    private By loginButton = By.id("loginBtn");
    private By errorMessage = By.id("errorMsg");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        seleniumUtils = new SeleniumUtils(driver);
        waitUtils = new WaitUtils(driver);
    }

    public void enterUsername(String username) {
        seleniumUtils.typeText(driver.findElement(usernameField), username);
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
