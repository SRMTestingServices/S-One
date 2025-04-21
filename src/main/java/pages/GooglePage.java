package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.WebDriverUtils;

public class GooglePage {
    private WebDriver driver;
    private WebDriverUtils utils;


    private By searchBox = By.id("APjFqb");
    private By passwordField = By.id("password");
    private By loginButton = By.id("loginBtn");
    private By errorMessage = By.id("errorMsg");

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
