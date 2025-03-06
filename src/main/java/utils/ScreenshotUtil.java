package utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtil {
    private static final String SCREENSHOT_DIR = "reports/screenshots/";

    public static String captureScreenshot(WebDriver driver, String testName) {
        if (!(driver instanceof TakesScreenshot)) {
            throw new IllegalArgumentException("Driver does not support screenshots");
        }

        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        try {
            // Create directory if it doesn't exist
            Files.createDirectories(Paths.get(SCREENSHOT_DIR));

            // Generate timestamped filename
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String screenshotPath = SCREENSHOT_DIR + testName + "_" + timestamp + ".png";

            // Save screenshot
            FileUtils.copyFile(srcFile, new File(screenshotPath));

            return screenshotPath;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save screenshot: " + e.getMessage(), e);
        }
    }
}
