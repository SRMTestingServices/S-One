package utils;

import org.openqa.selenium.*;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtil {
    private static final String SCREENSHOT_DIR = "target/reports/screenshots/";

    public static String captureScreenshot(WebDriver driver, String prefix) {
        try {
            // Convert to absolute path and normalize
            String absolutePath = new File(SCREENSHOT_DIR).getAbsolutePath();
            Path screenshotDir = Paths.get(absolutePath);

            // Create directory if needed
            Files.createDirectories(screenshotDir);

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(new Date());
            String threadId = String.valueOf(Thread.currentThread().getId());
            String filename = String.format("%s_%s_%s.png",
                    prefix != null ? prefix : "screenshot",
                    timestamp,
                    Thread.currentThread().getId());

            File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
            File destFile = screenshotDir.resolve(filename).toFile();
            FileUtils.copyFile(srcFile, destFile);

            return SCREENSHOT_DIR + filename;
        } catch (Exception e) {
            System.err.println("Failed to capture screenshot: " + e.getMessage());
            return "screenshot_failed.png";
        }
    }
}