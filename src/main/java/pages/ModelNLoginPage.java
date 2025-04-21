package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.WebDriverUtils;

public class ModelNLoginPage {
    private WebDriver driver;
    private WebDriverUtils utils;


    private final By userName = By.xpath("//input[@name='~123~-login-tbUsername0']");
    private final By passwordField = By.xpath("//input[@name='~123~-login-tbPassword0']");
    private final By loginButton = By.xpath("//*[@name='~123~-login-btLogin0']");

    public ModelNLoginPage(WebDriver driver) {
        this.driver = driver;
        this.utils = new WebDriverUtils(driver);
    }

    public void clickLoginButton() {
        utils.click(loginButton);
    }
    public void enterUserName(String searchText) {
        utils.enterText(userName, searchText);
    }

    public void enterPassword(String password) {
        utils.enterText(passwordField, password);
    }

}
