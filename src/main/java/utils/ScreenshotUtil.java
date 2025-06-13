package utils;

import base.DriverManager;
import org.openqa.selenium.*;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.chrome.ChromeDriver;
import reporting.ReportManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class ScreenshotUtil {
    public static class ScreenshotResult {
        public final String fullPath;
        public final String filename;

        public ScreenshotResult(String fullPath, String filename) {
            this.fullPath = fullPath;
            this.filename = filename;
        }
    }
    public static String captureScreenshot(String prefix) {
        WebDriver driver = DriverManager.getDriver();
        if (driver == null) return null;

        try {
            long timestamp = System.currentTimeMillis();
            String filename = prefix + "_" + timestamp + ".png";
            String fullPath = ReportManager.getScreenshotDirectory() + filename;

            Files.createDirectories(Paths.get(ReportManager.getScreenshotDirectory()));
            File srcFile;
            if (driver instanceof TakesScreenshot) {
                srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            }
            else {
                throw new UnsupportedOperationException("Driver does not support screenshot capture");
            }
            FileUtils.copyFile(srcFile, new File(fullPath));

            // Return consistent file URL format
            return "file:///" + fullPath.replace("\\", "/");
        } catch (Exception e) {
            System.err.println("Error capturing screenshot: " + e.getMessage());
            return null;
        }
    }

    public static String captureFullPageScreenshot(String prefix) {
        WebDriver driver = DriverManager.getDriver();
        try {
            // Only works with ChromeDriver
            if (driver instanceof ChromeDriver) {
                File srcFile = ((ChromeDriver) driver).getScreenshotAs(OutputType.FILE);
                String screenshotPath = ReportManager.getScreenshotPath(prefix != null ? prefix : "fullpage");
                FileUtils.copyFile(srcFile, new File(screenshotPath));
                return screenshotPath;
            }
            return captureScreenshot(prefix); // fallback to regular screenshot
        } catch (Exception e) {
            System.err.println("Failed to capture full page screenshot: " + e.getMessage());
            return captureScreenshot(prefix); // fallback to regular screenshot
        }
    }
}