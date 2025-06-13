package base;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.Browser;
import utils.ConfigReader;

import java.net.MalformedURLException;
import java.net.URL;

public class DriverManager {
	private static final ThreadLocal<WebDriver> webDriverThread = new ThreadLocal<>();
	private static final ThreadLocal<AppiumDriver> mobileDriverThread = new ThreadLocal<>();
	private static final String PLATFORM = ConfigReader.getProperty("platform").toLowerCase();

	public static WebDriver getDriver() {
		if (PLATFORM.equalsIgnoreCase("web")) {
			if (webDriverThread.get() == null) {
				initializeWebDriver();
			}
			return webDriverThread.get();
		} else {
			if (mobileDriverThread.get() == null) {
				mobileDriverThread.set(initializeMobileDriver());
			}
			return mobileDriverThread.get();
		}
	}

	private static void initializeWebDriver() {
		WebDriverManager.chromedriver().setup();
		ChromeOptions options = new ChromeOptions();

		if (ConfigReader.getBooleanProperty("headless")) {
			options.addArguments("--headless=new");
		}
		if (ConfigReader.getBooleanProperty("remote.allow.origins")) {
			options.addArguments("--remote-allow-origins=*");
		}
		if (ConfigReader.getBooleanProperty("start.maximized")) {
			options.addArguments("--start-maximized");
		}

		webDriverThread.set(new ChromeDriver(options));
	}
	private static AppiumDriver initializeMobileDriver() {
		String appiumUrl = ConfigReader.getProperty("appium.server.url");

		if (PLATFORM.equals("android")) {
			UiAutomator2Options options = new UiAutomator2Options()
					.setPlatformName(ConfigReader.getProperty("android.platform.name"))
					.setDeviceName(ConfigReader.getProperty("android.device.name"))
					.setAutomationName(ConfigReader.getProperty("android.automation.name"))
					.setApp(ConfigReader.getProperty("android.app.path"));

			try {
				return new AndroidDriver(new URL(appiumUrl), options);
			} catch (MalformedURLException e) {
				throw new RuntimeException("Invalid Appium Server URL: " + appiumUrl);
			}
		} else if (PLATFORM.equals("ios")) {
			XCUITestOptions options = new XCUITestOptions()
					.setPlatformName(ConfigReader.getProperty("ios.platform.name"))
					.setDeviceName(ConfigReader.getProperty("ios.device.name"))
					.setApp(ConfigReader.getProperty("ios.app.path"));

			try {
				return new IOSDriver(new URL(appiumUrl), options);
			} catch (MalformedURLException e) {
				throw new RuntimeException("Invalid Appium Server URL: " + appiumUrl);
			}
		}
		throw new RuntimeException("Unsupported platform: " + PLATFORM);
	}

	public static void quitDriver() {
		if (webDriverThread.get() != null) {
			webDriverThread.get().quit();
			webDriverThread.remove();
		}
		if (mobileDriverThread.get() != null) {
			mobileDriverThread.get().quit();
			mobileDriverThread.remove();
		}
	}
	public static String getBrowserName()
	{
		return ConfigReader.getProperty("browser").toUpperCase();
	}
}
