package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class SeleniumUtils {

    private WebDriver driver;

    public SeleniumUtils(WebDriver driver) {
        this.driver = driver;
    }

    public void clickElement(WebElement element) {
        element.click();
    }

    public void typeText(WebElement element, String text) {
        element.clear();
        element.sendKeys(text);
    }

    public String getElementText(WebElement element) {
        return element.getText();
    }

    public void selectDropdownByValue(WebElement element, String value) {
        new Select(element).selectByValue(value);
    }

    public void selectDropdownByText(WebElement element, String text) {
        new Select(element).selectByVisibleText(text);
    }
}
