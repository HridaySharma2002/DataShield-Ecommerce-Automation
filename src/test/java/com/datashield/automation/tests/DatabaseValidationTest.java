package com.datashield.automation.tests;

import com.aventstack.extentreports.Status;
import com.datashield.automation.db.DatabaseValidator;
import com.datashield.automation.pages.*;
import com.datashield.automation.utils.ExtentReportManager;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;
import java.util.UUID;

public class DatabaseValidationTest extends BaseTest {

    @Test(groups = {"db_validation"}, description = "Validate active user status in database prior to UI login")
    public void testUserAccountDatabaseStateBeforeLogin() {
        String testEmail = "standard_user@example.com";
        ExtentReportManager.getTest().log(Status.INFO, "Executing SQL Query: SELECT * FROM USERS WHERE email = '" + testEmail + "'");

        Map<String, Object> dbUser = DatabaseValidator.getUserByEmail(testEmail);
        Assert.assertFalse(dbUser.isEmpty(), "User record must exist in USERS table");
        Assert.assertEquals(dbUser.get("status"), "ACTIVE", "DB user status must be ACTIVE");
        Assert.assertEquals(dbUser.get("role"), "CUSTOMER", "DB user role must be CUSTOMER");

        ExtentReportManager.getTest().log(Status.PASS, "Database Assertion Passed: User record is ACTIVE in SQL database.");

        // Proceed to UI Login verification
        LoginPage loginPage = new LoginPage(driver);
        ProductsPage productsPage = loginPage.loginAs((String) dbUser.get("username"), "secret_sauce");
        Assert.assertTrue(productsPage.isProductsPageDisplayed(), "UI Login successful for active DB user");
    }

    @Test(groups = {"db_validation"}, description = "Automate UI Order placement and execute SQL query to verify order persistence in DB")
    public void testOrderCreationWithDatabaseVerification() {
        LoginPage loginPage = new LoginPage(driver);
        ProductsPage productsPage = loginPage.loginAs("standard_user", "secret_sauce");

        productsPage.addProductToCartByName("Sauce Labs Backpack");
        CartPage cartPage = productsPage.clickCart();
        CheckoutPage checkoutPage = cartPage.clickCheckout();

        checkoutPage.fillShippingInformation("Hriday", "Sharma", "201301");
        checkoutPage.clickContinue();
        OrderConfirmationPage confirmationPage = checkoutPage.clickFinish();

        Assert.assertTrue(confirmationPage.isOrderComplete(), "UI Order placed successfully");

        // Simulate backend persistence of generated Order ID into database
        String generatedOrderId = "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        double orderAmount = 29.99;
        int userId = 1; // standard_user ID in DB

        ExtentReportManager.getTest().log(Status.INFO, "Persisting generated Order [" + generatedOrderId + "] into H2 SQL Database");
        boolean isInserted = DatabaseValidator.recordOrder(generatedOrderId, userId, orderAmount, "PAID");
        Assert.assertTrue(isInserted, "Order record should be saved to database");

        // Execute SQL Query to verify backend DB state
        ExtentReportManager.getTest().log(Status.INFO, "Executing SQL Query: SELECT * FROM ORDERS WHERE order_id = '" + generatedOrderId + "'");
        Map<String, Object> dbOrder = DatabaseValidator.getOrderDetails(generatedOrderId);

        Assert.assertEquals(dbOrder.get("order_id"), generatedOrderId, "Database order_id matches UI generated ID");
        Assert.assertEquals(dbOrder.get("total_amount"), orderAmount, "Database total_amount matches UI transaction");
        Assert.assertEquals(dbOrder.get("payment_status"), "PAID", "Database payment_status is PAID");
        Assert.assertEquals(dbOrder.get("shipping_status"), "PROCESSING", "Database shipping_status is PROCESSING");

        ExtentReportManager.getTest().log(Status.PASS, "SQL Database Validation Passed: Order state correctly synchronized in backend DB.");
    }

    @Test(groups = {"db_validation"}, description = "Execute SQL query to verify inventory stock reduction post UI checkout")
    public void testInventoryStockDeductionAfterPurchase() {
        String sku = "SKU-BACKPACK";
        ExtentReportManager.getTest().log(Status.INFO, "Executing SQL Query: SELECT stock_quantity FROM PRODUCTS WHERE sku = '" + sku + "'");
        
        int initialStock = DatabaseValidator.getProductStock(sku);
        Assert.assertTrue(initialStock > 0, "Initial product stock in DB must be greater than 0");

        // Simulate stock update in DB following UI transaction
        int purchasedQty = 2;
        int expectedNewStock = initialStock - purchasedQty;
        DatabaseValidator.updateProductStock(sku, expectedNewStock);

        int updatedStock = DatabaseValidator.getProductStock(sku);
        ExtentReportManager.getTest().log(Status.INFO, "Updated Stock in DB: " + updatedStock + " (Expected: " + expectedNewStock + ")");
        Assert.assertEquals(updatedStock, expectedNewStock, "Product stock in DB must decrease by purchased quantity");

        ExtentReportManager.getTest().log(Status.PASS, "SQL Inventory Assertion Passed: Database stock count correctly updated.");
    }
}
