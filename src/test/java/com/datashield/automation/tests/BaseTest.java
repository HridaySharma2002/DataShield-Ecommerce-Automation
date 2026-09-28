package com.datashield.automation.tests;

import com.datashield.automation.config.ConfigManager;
import com.datashield.automation.db.DBConnectionManager;
import com.datashield.automation.server.MockEcommerceServer;
import com.datashield.automation.utils.DriverManager;
import com.datashield.automation.utils.ScreenshotUtils;

import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.lang.reflect.Method;

public abstract class BaseTest {

    protected WebDriver driver;

    @BeforeSuite(alwaysRun = true)
    public void setupSuite() {
        System.out.println("Initializing Database Connection Pool for Test Suite...");
        DBConnectionManager.getConnection();
        String appUrl = ConfigManager.get("app.url");
        if (appUrl != null && appUrl.contains("localhost")) {
            MockEcommerceServer.startServer();
        }
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        DriverManager.initDriver();
        driver = DriverManager.getDriver();
        driver.get(ConfigManager.get("app.url"));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result, Method method) {
        if (driver != null) {
            String testName = method.getName();
            String status = result.isSuccess() ? "PASSED" : "FAILED";
            ScreenshotUtils.saveScreenshotToFile(driver, testName + "_" + status);
        }
        DriverManager.quitDriver();
    }

    @AfterSuite(alwaysRun = true)
    public void tearDownSuite() {
        System.out.println("Closing Database Connection Pool...");
        DBConnectionManager.closeConnection();
        MockEcommerceServer.stopServer();
    }
}
