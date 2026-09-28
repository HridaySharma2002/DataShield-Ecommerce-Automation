package com.datashield.automation.tests;

import com.datashield.automation.config.ConfigManager;
import com.datashield.automation.db.DBConnectionManager;
import com.datashield.automation.utils.DriverManager;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

public abstract class BaseTest {

    protected WebDriver driver;

    @BeforeSuite(alwaysRun = true)
    public void setupSuite() {
        System.out.println("Initializing Database Connection Pool for Test Suite...");
        DBConnectionManager.getConnection();
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        DriverManager.initDriver();
        driver = DriverManager.getDriver();
        driver.get(ConfigManager.get("app.url"));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverManager.quitDriver();
    }

    @AfterSuite(alwaysRun = true)
    public void tearDownSuite() {
        System.out.println("Closing Database Connection Pool...");
        DBConnectionManager.closeConnection();
    }
}
