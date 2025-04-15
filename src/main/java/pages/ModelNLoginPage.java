package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.SeleniumUtils;
import utils.WaitUtils;

public class ModelNLoginPage {
    private WebDriver driver;
    private SeleniumUtils seleniumUtils;
    private WaitUtils waitUtils;


    private final By userName = By.xpath("//input[@name='~123~-login-tbUsername0']");
    private final By passwordField = By.xpath("//input[@name='~123~-login-tbPassword0']");
    private final By loginButton = By.xpath("//*[@name='~123~-login-btLogin0']");

    public ModelNLoginPage(WebDriver driver) {
        this.driver = driver;
        seleniumUtils = new SeleniumUtils(driver);
        waitUtils = new WaitUtils(driver);
    }

    public void clickLoginButton() {
        waitUtils.waitForElementToBeClickable(driver.findElement(loginButton), 10);
        seleniumUtils.clickElement(driver.findElement(loginButton));
    }
    public void enterUserName(String searchText) {
        seleniumUtils.typeText(driver.findElement(userName), searchText);
    }

    public void enterPassword(String password) {
        seleniumUtils.typeText(driver.findElement(passwordField), password);
    }

}
