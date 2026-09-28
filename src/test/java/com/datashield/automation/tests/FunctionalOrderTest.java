package com.datashield.automation.tests;

import com.datashield.automation.pages.*;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FunctionalOrderTest extends BaseTest {

    @Test(groups = {"functional", "regression"}, description = "E2E Checkout Workflow: Browse, Add to Cart, Shipping Details, and Order Placement")
    public void testEndToEndProductCheckout() {
        LoginPage loginPage = new LoginPage(driver);
        ProductsPage productsPage = loginPage.loginAs("standard_user", "secret_sauce");

        // Add Product to Cart
        productsPage.addProductToCartByName("Sauce Labs Backpack");
        Assert.assertEquals(productsPage.getCartBadgeCount(), 1, "Cart badge should reflect 1 added item");

        // Navigate to Cart
        CartPage cartPage = productsPage.clickCart();
        Assert.assertTrue(cartPage.isCartPageDisplayed(), "Cart page should be displayed");
        Assert.assertEquals(cartPage.getCartItemCount(), 1, "Cart item count should match added item");

        // Proceed to Checkout
        CheckoutPage checkoutPage = cartPage.clickCheckout();
        checkoutPage.fillShippingInformation("Hriday", "Sharma", "110001");
        checkoutPage.clickContinue();

        // Finish Order
        OrderConfirmationPage confirmationPage = checkoutPage.clickFinish();
        Assert.assertTrue(confirmationPage.isOrderComplete(), "Order completion header should be visible");
    }
}
