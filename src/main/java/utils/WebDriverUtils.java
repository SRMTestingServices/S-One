package utils;

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

	/** Retries the given action up to RETRY_COUNT times if exceptions occur. */
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

	/** Clicks the element using WebDriverWait with retry logic. */
	public void click(By element) {
		retryAction(() -> wait.until(ExpectedConditions.elementToBeClickable(element)).click());
	}

	/** Tries normal click, falls back to JavaScript click on failure. */
	public void resilientClick(By element) {
		try {
			click(element);
		} catch (Exception e) {
			try {
				((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
			} catch (Exception ex) {
				throw new RuntimeException("Both normal and JS click failed.", ex);
			}
		}
	}

	/** Clicks the given WebElement using JavaScriptExecutor. */
	public void clickUsingJS(WebElement element) {
		retryAction(() -> js.executeScript("arguments[0].click();", element));
	}

	/** Clears existing text and enters the given text into the element. */
	public void enterText(By element, String text) {
		retryAction(() -> {
			WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(element));
			el.clear();
			el.sendKeys(text);
		});
	}

	/** Clears text using Ctrl+A and Delete key combination. */
	public void clearTextKeys(WebElement element) {
		retryAction(() -> element.sendKeys(Keys.CONTROL + "a", Keys.DELETE));
	}

	/** Clears the input field using the clear() method. */
	public void clearText(WebElement element) {
		element.clear();
	}

	/** Returns the visible text of the specified element. */
	public String getText(By element) {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(element)).getText();
	}

	/** Selects a dropdown option by visible text. */
	public void selectDropdownByText(By element, String text) {
		retryAction(() -> new Select(driver.findElement(element)).selectByVisibleText(text));
	}

	/** Selects a dropdown option by its value attribute. */
	public void selectDropdownByValue(By element, String value) {
		retryAction(() -> new Select(driver.findElement(element)).selectByValue(value));
	}

	/** Checks if the element is visible on the page. */
	public boolean isDisplayed(By element) {
		try {
			return wait.until(ExpectedConditions.visibilityOfElementLocated(element)).isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	/** Checks if the element is clickable. */
	public boolean isClickable(By element) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(element));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/** Scrolls the given element into view. */
	public void scrollToElement(WebElement element) {
		js.executeScript("arguments[0].scrollIntoView(true);", element);
	}

	/** Verifies if the given text is present on the page. */
	public boolean isTextPresent(String text) {
		return driver.findElement(By.tagName("body")).getText().contains(text);
	}

	/** Waits for the page to completely load. */
	public void waitForPageToLoad() {
		wait.until(webDriver -> Objects.equals(js.executeScript("return document.readyState"), "complete"));
	}

	/** Highlights the given element by adding a red border. */
	public void highlightElement(WebElement element) {
		js.executeScript("arguments[0].style.border='3px solid red'", element);
	}

	/** Returns the current URL of the browser. */
	public String getCurrentURL() {
		return driver.getCurrentUrl();
	}

	/** Switches to a frame using its locator. */
	public void switchToFrame(By frameElement) {
		driver.switchTo().frame(driver.findElement(frameElement));
	}

	/** Switches to a frame using its ID. */
	public void switchToFrameByID(String frameId) {
		driver.switchTo().frame(frameId);
	}

	/** Switches back to the default content from a frame. */
	public void switchToDefaultContent() {
		driver.switchTo().defaultContent();
	}

	/** Deletes all browser cookies. */
	public void deleteCookies() {
		driver.manage().deleteAllCookies();
	}

	/** Returns a formatted timestamp string. */
	public static String getCurrentTimestamp() {
		return new SimpleDateFormat("ddMMyyHHmmss").format(Calendar.getInstance().getTime());
	}

	/** Refreshes the current page. */
	public void refreshPage() {
		driver.navigate().refresh();
	}

	/** Performs a mouse hover over the specified element. */
	public void hoverOver(WebElement element) {
		actions.moveToElement(element).perform();
	}

	/** Drags and drops one element onto another. */
	public void dragAndDrop(WebElement source, WebElement target) {
		actions.clickAndHold(source).moveToElement(target).release().build().perform();
	}

	/** Returns the title of the current page. */
	public String getTitle() {
		return driver.getTitle();
	}

	/** Sets the implicit wait timeout in seconds. */
	public void implicitWait(int seconds) {
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(seconds));
	}

	/** Waits for the element to be visible. */
	public void waitForElementToBeVisible(By element) {
		retryAction(() -> wait.until(ExpectedConditions.visibilityOfElementLocated(element)));
	}

	/** Returns the value of the specified attribute of the element. */
	public String getAttribute(WebElement element, String attribute) {
		return element.getDomAttribute(attribute);
	}

	/** Checks if an alert is present. */
	public boolean isAlertPresent() {
		try {
			wait.until(ExpectedConditions.alertIsPresent());
			return true;
		} catch (TimeoutException e) {
			return false;
		}
	}

	/** Accepts the currently displayed alert. */
	public void acceptAlert() {
		driver.switchTo().alert().accept();
	}

	/** Dismisses the currently displayed alert. */
	public void dismissAlert() {
		driver.switchTo().alert().dismiss();
	}

	/** Returns the text of the currently displayed alert. */
	public String getAlertText() {
		return driver.switchTo().alert().getText();
	}

	/** Navigates to the specified URL. */
	public void navigateTo(String url) {
		driver.navigate().to(url);
	}

	/** Switches to the last opened browser window. */
	public void switchToLastWindow() {
		parentWindowHandler = driver.getWindowHandle();
		Set<String> handles = driver.getWindowHandles();
		for (String handle : handles) {
			driver.switchTo().window(handle);
		}
		waitForPageToLoad();
	}

	/** Switches back to the original parent browser window. */
	public void switchToParentWindow() {
		driver.switchTo().window(parentWindowHandler);
		waitForPageToLoad();
	}

	/** Performs a double click on the specified element. */
	public void doubleClick(WebElement element) {
		actions.doubleClick(element).perform();
	}

	/** Performs a right-click on the specified element. */
	public void rightClick(WebElement element) {
		actions.contextClick(element).perform();
	}

	/** Sends the specified key(s) to the element. */
	public void sendKeys(WebElement element, Keys key) {
		element.sendKeys(key);
	}

	/** Waits until the specified element is no longer visible. */
	public void waitForInvisibility(By locator) {
		wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
	}

	/** Waits for an attribute to have a specific value. */
	public void waitForAttributeToBe(By locator, String attribute, String value) {
		wait.until(ExpectedConditions.attributeToBe(locator, attribute, value));
	}

	/** Waits for text to be present in the specified element. */
	public void waitForTextToBePresent(By locator, String text) {
		wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
	}

	/** Scrolls the page by specified x and y offset. */
	public void scrollBy(int x, int y) {
		js.executeScript("window.scrollBy(arguments[0], arguments[1]);", x, y);
	}

	/** Sets an attribute on the specified element using JavaScript. */
	public void setAttribute(WebElement element, String attribute, String value) {
		js.executeScript("arguments[0].setAttribute(arguments[1], arguments[2]);", element, attribute, value);
	}

	/** Checks if the specified element is enabled. */
	public boolean isEnabled(By locator) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(locator)).isEnabled();
	}

	/** Checks if the specified element is selected. */
	public boolean isSelected(By locator) {
		return wait.until(ExpectedConditions.presenceOfElementLocated(locator)).isSelected();
	}

	/** Verifies if element's text equals the expected value. */
	public boolean verifyTextEquals(By locator, String expectedText) {
		return getText(locator).equals(expectedText);
	}

	/** Checks if the current URL contains the specified partial string. */
	public boolean isURLContains(String partialURL) {
		return Objects.requireNonNull(driver.getCurrentUrl()).contains(partialURL);
	}

	/** Switches to the browser window with the given title. */
	public void switchToWindowByTitle(String title) {
		for (String handle : driver.getWindowHandles()) {
			driver.switchTo().window(handle);
			if (Objects.equals(driver.getTitle(), title)) {
				return;
			}
		}
		throw new NoSuchWindowException("Window with title '" + title + "' not found.");
	}

	/** Returns the CSS value of the given property for the element. */
	public String getCssValue(WebElement element, String propertyName) {
		return element.getCssValue(propertyName);
	}

}
