package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.WebDriverUtils;

public class LoginPage {
    private WebDriver driver;
    private WebDriverUtils utils;


    private By usernameField = By.id("username");
    private By passwordField = By.id("password");
    private By loginButton = By.id("loginBtn");
    private By errorMessage = By.id("errorMsg");

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
