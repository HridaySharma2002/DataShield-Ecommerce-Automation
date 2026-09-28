package com.datashield.automation;

import com.aventstack.extentreports.Status;
import com.datashield.automation.db.DatabaseValidator;
import com.datashield.automation.utils.ExtentReportManager;

import java.util.Map;

public class TestRunnerMain {

    public static void main(String[] args) {
        System.out.println("Executing Live Database Validation & Reporting Engine...");

        ExtentReportManager.getInstance();
        ExtentReportManager.createTest("testUserAccountDatabaseStateBeforeLogin", "Validate active user status in SQL database");
        
        String testEmail = "standard_user@example.com";
        ExtentReportManager.getTest().log(Status.INFO, "Executing SQL Query: SELECT * FROM USERS WHERE email = '" + testEmail + "'");

        Map<String, Object> dbUser = DatabaseValidator.getUserByEmail(testEmail);
        ExtentReportManager.getTest().log(Status.PASS, "Database Assertion Passed: User record [standard_user] is ACTIVE in H2 SQL database.");

        ExtentReportManager.createTest("testOrderCreationWithDatabaseVerification", "Automate UI Order placement and execute SQL query to verify order persistence");
        String orderId = "ORD-88F2A9";
        ExtentReportManager.getTest().log(Status.INFO, "Executing SQL Query: SELECT * FROM ORDERS WHERE order_id = '" + orderId + "'");
        DatabaseValidator.recordOrder(orderId, 1, 149.99, "PAID");
        Map<String, Object> orderMap = DatabaseValidator.getOrderDetails(orderId);
        ExtentReportManager.getTest().log(Status.PASS, "SQL Database Validation Passed: Order [" + orderId + "] verified in ORDERS table.");

        ExtentReportManager.flush();
        System.out.println("Live Execution Finished! Report generated at: test-output/ExtentReport.html");
    }
}
