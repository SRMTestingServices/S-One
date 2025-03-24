package utils;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import base.DriverManager;


/*
 * Webdriver library to handle all selenium actions 
 * 
 * @author Selva 
 */
public class WebDriverLibrary {

    private WebDriver driver = DriverManager.getDriver();
    private WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    private Actions actions = new Actions(driver);
    private JavascriptExecutor js = (JavascriptExecutor) driver;

    public static String parentWindowHandler;

    /** Get text from an element */
    public String getText(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element)).getText();
    }

    /** Click element */
    public void click(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    /** Enter text in an input field */
    public void enterText(WebElement element, String text) {
        wait.until(ExpectedConditions.visibilityOf(element)).sendKeys(text);
    }

    /** Clear text field */
    public void clearText(WebElement element) {
        element.sendKeys(Keys.CONTROL + "a", Keys.DELETE);
 
    }

    /** Select dropdown by visible text */
    public void selectDropdownByText(WebElement element, String text) {
        new Select(element).selectByVisibleText(text);
    }

    /** Select dropdown by value */
    public void selectDropdownByValue(WebElement element, String value) {
        new Select(element).selectByValue(value);
    }

    /** Verify if an element is displayed */
    public boolean isDisplayed(WebElement element) {
        try {
            return wait.until(ExpectedConditions.visibilityOf(element)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /** Verify if an element is clickable */
    public boolean isClickable(WebElement element) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** Switch to the last opened window */
    public void switchToLastWindow() {
        parentWindowHandler = driver.getWindowHandle();
        Set<String> handles = driver.getWindowHandles();
        for (String handle : handles) {
            driver.switchTo().window(handle);
        }
        waitForPageToLoad();
    }

    /** Switch back to parent window */
    public void switchToParentWindow() {
        driver.switchTo().window(parentWindowHandler);
        waitForPageToLoad();
    }

    /** Scroll to an element */
    public void scrollToElement(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /** Click element using JavaScript */
    public void clickUsingJS(WebElement element) {
        js.executeScript("arguments[0].click();", element);
    }

    /** Verify if text is present on the page */
    public boolean isTextPresent(String text) {
        return driver.findElement(By.tagName("body")).getText().contains(text);
    }

    /** Wait for page to load */
    public void waitForPageToLoad() {
        new WebDriverWait(driver, Duration.ofSeconds(30)).until(
            webDriver -> js.executeScript("return document.readyState").equals("complete"));
    }

    /** Highlight an element */
    public void highlightElement(WebElement element) {
        js.executeScript("arguments[0].style.border='3px solid red'", element);
    }

    /** Get current page URL */
    public String getCurrentURL() {
        return driver.getCurrentUrl();
    }

    /** Switch to iframe */
    public void switchToFrame(WebElement frameElement) {
        driver.switchTo().frame(frameElement);
    }

    /** Switch back to default content */
    public void switchToDefaultContent() {
        driver.switchTo().defaultContent();
    }

    /** Delete all cookies */
    public void deleteCookies() {
        driver.manage().deleteAllCookies();
    }

    /** Get current timestamp */
    public static String getCurrentTimestamp() {
        return new SimpleDateFormat("ddMMyyHHmmss").format(Calendar.getInstance().getTime());
    }

    /** Refresh the page */
    public void refreshPage() {
        driver.navigate().refresh();
    }

    /** Hover over an element */
    public void hoverOver(WebElement element) {
        actions.moveToElement(element).perform();
    }

    /** Drag and drop an element */
    public void dragAndDrop(WebElement source, WebElement target) {
        actions.clickAndHold(source).moveToElement(target).release().build().perform();
    }

    /** Get page title */
    public String getTitle() {
        return driver.getTitle();
    }

    /** Implicit wait */
    public void implicitWait(int seconds) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(seconds));
    }

    /** Explicit wait for element to be visible */
    public void waitForElementToBeVisible(WebElement element, int seconds) {
        new WebDriverWait(driver, Duration.ofSeconds(seconds)).until(ExpectedConditions.visibilityOf(element));
    }

    /** Get attribute value */
    public String getAttribute(WebElement element, String attribute) {
        return element.getAttribute(attribute);
    }

    /** Check if an alert is present */
    public boolean isAlertPresent() {
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    /** Switch to alert and accept */
    public void acceptAlert() {
        driver.switchTo().alert().accept();
    }

    /** Switch to alert and dismiss */
    public void dismissAlert() {
        driver.switchTo().alert().dismiss();
    }

    /** Get alert text */
    public String getAlertText() {
        return driver.switchTo().alert().getText();
    }

    /** Navigate to a given URL */
    public void navigateTo(String url) {
        driver.navigate().to(url);
    }
}
