package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class WaitUtils {

    private WebDriver driver;

    public WaitUtils(WebDriver driver) {
        this.driver = driver;
    }

    public void waitForElementToBeClickable(WebElement element, int timeout) {
        new WebDriverWait(driver, Duration.ofSeconds(timeout))
                .until(ExpectedConditions.elementToBeClickable(element));
    }

    public void waitForElementToBeVisible(WebElement element, int timeout) {
        new WebDriverWait(driver, Duration.ofSeconds(timeout))
                .until(ExpectedConditions.visibilityOf(element));
    }
}
