package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.WebDriverUtils;

public class GooglePage {
    private final WebDriver driver;
    private final WebDriverUtils utils;


    private final By searchBox = By.id("APjFqb");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("loginBtn");
    private final By errorMessage = By.id("errorMsg");

    public GooglePage(WebDriver driver) {
        this.driver = driver;
        this.utils = new WebDriverUtils(driver);
    }

    public void enterSearchBox(String searchText) {
        utils.enterText(searchBox, searchText);
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
