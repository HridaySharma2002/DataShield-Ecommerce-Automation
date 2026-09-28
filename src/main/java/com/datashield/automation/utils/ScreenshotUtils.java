package com.datashield.automation.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ScreenshotUtils {

    public static String captureBase64Screenshot(WebDriver driver) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            return ts.getScreenshotAs(OutputType.BASE64);
        } catch (Exception e) {
            System.err.println("Failed to capture base64 screenshot: " + e.getMessage());
            return "";
        }
    }

    public static String saveScreenshotToFile(WebDriver driver, String screenshotName) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File src = ts.getScreenshotAs(OutputType.FILE);
            File destDir = new File("screenshots");
            if (!destDir.exists()) {
                destDir.mkdirs();
            }
            File dest = new File(destDir, screenshotName + ".png");
            Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Screenshot captured: " + dest.getAbsolutePath());
            return dest.getAbsolutePath();
        } catch (Exception e) {
            System.err.println("Failed to save screenshot file: " + e.getMessage());
            return "";
        }
    }
}
