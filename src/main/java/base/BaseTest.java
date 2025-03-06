package base;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.net.MalformedURLException;
import java.net.URL;

public class BaseTest {
    protected WebDriver driver;
    protected AppiumDriver mobileDriver;

    @BeforeMethod
    public void setup() throws MalformedURLException {
        String platform = System.getProperty("platform");
        if ("web".equalsIgnoreCase(platform)) {
            driver = new ChromeDriver();
        } else if ("android".equalsIgnoreCase(platform)) {
            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setCapability("platformName", "Android");
            mobileDriver = new AndroidDriver(new URL("http://localhost:4723/wd/hub"), caps);
        }
    }

    @AfterMethod
    public void teardown() {
        if (driver != null) driver.quit();
        if (mobileDriver != null) mobileDriver.quit();
    }
}
