package utils;

import base.DriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Objects;
import java.util.Set;

/**
 * Consolidated WebDriver utility class with retry logic and enhanced best practices
 */
public class WebDriverUtils {

	private WebDriver driver;
	private WebDriverWait wait;
	private Actions actions;
	private JavascriptExecutor js;

	private static final int DEFAULT_WAIT = 20;
	private static final int RETRY_COUNT = 3;
	public static String parentWindowHandler;

	public WebDriverUtils(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_WAIT));
		this.actions = new Actions(driver);
		this.js = (JavascriptExecutor) driver;
	}

	// Retry Wrapper
	private void retryAction(Runnable action) {
		int attempts = 0;
		while (attempts < RETRY_COUNT) {
			try {
				action.run();
				return;
			} catch (StaleElementReferenceException | ElementClickInterceptedException | TimeoutException e) {
				attempts++;
				if (attempts == RETRY_COUNT) throw e;
			}
		}
	}

	public void click(By element) {
		retryAction(() -> wait.until(ExpectedConditions.elementToBeClickable(element)).click());
	}

	public void resilientClick(By element) {
		try {
			click(element);
		} catch (Exception e) {
			// fallback to JavaScript click if regular click fails
			try {
				((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
			} catch (Exception ex) {
				throw new RuntimeException("Both normal and JS click failed.", ex);
			}
		}
	}
	public void clickUsingJS(WebElement element) {
		retryAction(() -> js.executeScript("arguments[0].click();", element));
	}

	public void enterText(By element, String text) {
		retryAction(() -> wait.until(ExpectedConditions.visibilityOfElementLocated(element)).sendKeys(text));
	}

	public void clearText(WebElement element) {
		retryAction(() -> element.sendKeys(Keys.CONTROL + "a", Keys.DELETE));
	}

	public String getText(By element) {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(element)).getText();
	}

	public void selectDropdownByText(By element, String text) {
		retryAction(() -> new Select(driver.findElement(element)).selectByVisibleText(text));
	}

	public void selectDropdownByValue(By element, String value) {
		retryAction(() -> new Select(driver.findElement(element)).selectByValue(value));
	}

	public boolean isDisplayed(By element) {
		try {
			return wait.until(ExpectedConditions.visibilityOfElementLocated(element)).isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isClickable(By element) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(element));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void scrollToElement(WebElement element) {
		js.executeScript("arguments[0].scrollIntoView(true);", element);
	}

	public boolean isTextPresent(String text) {
		return driver.findElement(By.tagName("body")).getText().contains(text);
	}

	public void waitForPageToLoad() {
		wait.until(
				webDriver -> Objects.equals(js.executeScript("return document.readyState"), "complete"));
	}

	public void highlightElement(WebElement element) {
		js.executeScript("arguments[0].style.border='3px solid red'", element);
	}

	public String getCurrentURL() {
		return driver.getCurrentUrl();
	}

	public void switchToFrame(By frameElement) {
		driver.switchTo().frame(driver.findElement(frameElement));
	}

	public void switchToFrameByID(String frameId) {
		driver.switchTo().frame(frameId);
	}

	public void switchToDefaultContent() {
		driver.switchTo().defaultContent();
	}

	public void deleteCookies() {
		driver.manage().deleteAllCookies();
	}

	public static String getCurrentTimestamp() {
		return new SimpleDateFormat("ddMMyyHHmmss").format(Calendar.getInstance().getTime());
	}

	public void refreshPage() {
		driver.navigate().refresh();
	}

	public void hoverOver(WebElement element) {
		actions.moveToElement(element).perform();
	}

	public void dragAndDrop(WebElement source, WebElement target) {
		actions.clickAndHold(source).moveToElement(target).release().build().perform();
	}

	public String getTitle() {
		return driver.getTitle();
	}

	public void implicitWait(int seconds) {
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(seconds));
	}

	public void waitForElementToBeVisible(By element) {
		retryAction(() ->wait.until(ExpectedConditions.visibilityOfElementLocated(element)));
	}

	public String getAttribute(WebElement element, String attribute) {
		return element.getAttribute(attribute);
	}

	public boolean isAlertPresent() {
		try {
			wait.until(ExpectedConditions.alertIsPresent());
			return true;
		} catch (TimeoutException e) {
			return false;
		}
	}

	public void acceptAlert() {
		driver.switchTo().alert().accept();
	}

	public void dismissAlert() {
		driver.switchTo().alert().dismiss();
	}

	public String getAlertText() {
		return driver.switchTo().alert().getText();
	}

	public void navigateTo(String url) {
		driver.navigate().to(url);
	}

	public void switchToLastWindow() {
		parentWindowHandler = driver.getWindowHandle();
		Set<String> handles = driver.getWindowHandles();
		for (String handle : handles) {
			driver.switchTo().window(handle);
		}
		waitForPageToLoad();
	}

	public void switchToParentWindow() {
		driver.switchTo().window(parentWindowHandler);
		waitForPageToLoad();
	}
}
