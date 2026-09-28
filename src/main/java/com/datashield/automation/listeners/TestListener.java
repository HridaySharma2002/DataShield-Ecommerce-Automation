package com.datashield.automation.listeners;

import com.aventstack.extentreports.Status;
import com.datashield.automation.utils.DriverManager;
import com.datashield.automation.utils.ExtentReportManager;
import com.datashield.automation.utils.ScreenshotUtils;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        System.out.println("=== Starting Test Suite: " + context.getName() + " ===");
        ExtentReportManager.getInstance();
    }

    @Override
    public void onTestStart(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();
        ExtentReportManager.createTest(methodName, description != null ? description : methodName);
        ExtentReportManager.getTest().log(Status.INFO, "Started execution of test: " + methodName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentReportManager.getTest().log(Status.PASS, "Test PASSED: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentReportManager.getTest().log(Status.FAIL, "Test FAILED: " + result.getMethod().getMethodName());
        ExtentReportManager.getTest().log(Status.FAIL, result.getThrowable());

        try {
            String base64Screenshot = ScreenshotUtils.captureBase64Screenshot(DriverManager.getDriver());
            if (!base64Screenshot.isEmpty()) {
                ExtentReportManager.getTest().addScreenCaptureFromBase64String(base64Screenshot, "Failure Screenshot");
            }
        } catch (Exception e) {
            ExtentReportManager.getTest().log(Status.WARNING, "Failed to capture screenshot on failure: " + e.getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentReportManager.getTest().log(Status.SKIP, "Test SKIPPED: " + result.getMethod().getMethodName());
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("=== Finished Test Suite: " + context.getName() + " ===");
        ExtentReportManager.flush();
    }
}
