package com.datashield.automation.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.datashield.automation.config.ConfigManager;

public class ExtentReportManager {

    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> testThreadLocal = new ThreadLocal<>();

    public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            String reportPath = ConfigManager.get("report.path", "test-output/ExtentReport.html");
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);

            sparkReporter.config().setDocumentTitle(ConfigManager.get("report.title"));
            sparkReporter.config().setReportName(ConfigManager.get("report.name"));
            sparkReporter.config().setTheme(Theme.DARK);

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
            extent.setSystemInfo("Organization", "DataShield Automation Suite");
            extent.setSystemInfo("Environment", "QA Production Simulation");
            extent.setSystemInfo("Framework", "Java 21 + Selenium + TestNG + JDBC SQL");
            extent.setSystemInfo("Author", "Hriday Sharma");
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        }
        return extent;
    }

    public static synchronized ExtentTest createTest(String testName, String description) {
        ExtentTest test = getInstance().createTest(testName, description);
        testThreadLocal.set(test);
        return test;
    }

    public static synchronized ExtentTest getTest() {
        return testThreadLocal.get();
    }

    public static synchronized void flush() {
        if (extent != null) {
            extent.flush();
        }
    }
}
