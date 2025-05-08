package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.WebDriverUtils;

public class LoginPage {
    private final WebDriver driver;
    private final WebDriverUtils utils;


    private final By usernameField = By.id("username");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("loginBtn");
    private final By errorMessage = By.id("errorMsg");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.utils = new WebDriverUtils(driver);
    }

    public void enterUsername(String username) {
        utils.enterText(usernameField, username);
    }

    public void enterPassword(String password) {
        utils.enterText(passwordField, password);
    }

    public void clickLoginButton() {
        utils.click(loginButton);
    }

    public String getErrorMessage() {
        return utils.getText(errorMessage);
    }
}
