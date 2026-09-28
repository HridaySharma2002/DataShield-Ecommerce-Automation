package com.datashield.automation.tests;

import com.datashield.automation.pages.LoginPage;
import com.datashield.automation.pages.ProductsPage;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SmokeTest extends BaseTest {

    @Test(groups = {"smoke"}, description = "Verify Login Page renders correctly with logo and elements")
    public void testLoginPageRendering() {
        LoginPage loginPage = new LoginPage(driver);
        Assert.assertTrue(loginPage.isLoginPageDisplayed(), "Login page should be rendered with title and login button");
    }

    @Test(groups = {"smoke"}, description = "Smoke test for valid user login and navigation to Products page")
    public void testValidLoginSmoke() {
        LoginPage loginPage = new LoginPage(driver);
        ProductsPage productsPage = loginPage.loginAs("standard_user", "secret_sauce");
        Assert.assertTrue(productsPage.isProductsPageDisplayed(), "Products page should display after valid login");
        Assert.assertTrue(productsPage.getProductCount() > 0, "Product catalog should contain inventory items");
    }

    @Test(groups = {"smoke"}, description = "Verify invalid credentials display proper error message")
    public void testInvalidLoginSmoke() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginAs("invalid_user", "wrong_password");
        String errorMessage = loginPage.getErrorMessage();
        Assert.assertTrue(errorMessage.contains("Username and password do not match"), 
                "Error message should alert invalid credentials");
    }
}
