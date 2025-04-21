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

import java.net.MalformedURLException;
import java.net.URL;

public class DriverManager {
	private static final ThreadLocal<WebDriver> webDriverThread = new ThreadLocal<>();
	private static final ThreadLocal<AppiumDriver> mobileDriverThread = new ThreadLocal<>();
	private static final String PLATFORM = System.getProperty("platform", "web"); // Set via Maven CLI:
																					// -Dplatform=web/android/ios

	public static WebDriver getDriver() {
		if (PLATFORM.equalsIgnoreCase("web")) {
			if (webDriverThread.get() == null) {
				WebDriverManager.chromedriver().setup();
				ChromeOptions options = new ChromeOptions();
				options.addArguments("--disable-gpu");
				options.addArguments("--no-sandbox");
				options.addArguments("--remote-allow-origins=*");
				options.addArguments("--start-maximized");
				webDriverThread.set(new ChromeDriver(options));
			}
			return webDriverThread.get();
		} else {
			if (mobileDriverThread.get() == null) {
				mobileDriverThread.set(initializeMobileDriver());
			}
			return mobileDriverThread.get();
		}
	}

	private static AppiumDriver initializeMobileDriver() {
		if (PLATFORM.equalsIgnoreCase("android")) {
			UiAutomator2Options options = new UiAutomator2Options().setPlatformName("Android")
					.setDeviceName("emulator-5554").setApp("path/to/your/app.apk");

			try {
				return new AndroidDriver(new URL("http://localhost:4723/wd/hub"), options);
			} catch (MalformedURLException e) {
				throw new RuntimeException("Invalid Appium Server URL");
			}
		} else if (PLATFORM.equalsIgnoreCase("ios")) {
			XCUITestOptions options = new XCUITestOptions().setPlatformName("iOS").setDeviceName("iPhone 14")
					.setApp("path/to/your/app.app");

			try {
				return new IOSDriver(new URL("http://localhost:4723/wd/hub"), options);
			} catch (MalformedURLException e) {
				throw new RuntimeException("Invalid Appium Server URL");
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
}
