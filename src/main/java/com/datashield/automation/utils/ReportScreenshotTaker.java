package com.datashield.automation.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ReportScreenshotTaker {
    public static void main(String[] args) throws Exception {
        ChromeOptions opt = new ChromeOptions();
        opt.addArguments("--headless=new");
        opt.addArguments("--window-size=1920,1080");
        WebDriver driver = new ChromeDriver(opt);

        try {
            File reportFile = new File("test-output/ExtentReport.html");
            driver.get(reportFile.toURI().toString());
            Thread.sleep(2000);

            // Screenshot 1: Tests Detailed View
            File src1 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File dest1 = new File("screenshots/ExtentReport_TestsView.png");
            Files.copy(src1.toPath(), dest1.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Saved: " + dest1.getAbsolutePath());

            // Screenshot 2: Dashboard Analytics Charts View
            driver.findElement(By.id("nav-dashboard")).click();
            Thread.sleep(1500);
            File src2 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File dest2 = new File("screenshots/ExtentReport_DashboardCharts.png");
            Files.copy(src2.toPath(), dest2.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Saved: " + dest2.getAbsolutePath());

        } finally {
            driver.quit();
        }
    }
}
