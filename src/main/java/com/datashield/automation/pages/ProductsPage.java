package com.datashield.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ProductsPage extends BasePage {

    private final By pageTitle = By.className("title");
    private final By inventoryItems = By.className("inventory_item");
    private final By shoppingCartBadge = By.className("shopping_cart_badge");
    private final By shoppingCartLink = By.className("shopping_cart_link");

    public ProductsPage(WebDriver driver) {
        super(driver);
    }

    public boolean isProductsPageDisplayed() {
        return isDisplayed(pageTitle) && getText(pageTitle).equalsIgnoreCase("Products");
    }

    public int getProductCount() {
        return getElements(inventoryItems).size();
    }

    public void addProductToCartByName(String productName) {
        String formatButtonLocator = String.format("//div[text()='%s']/ancestor::div[@class='inventory_item']//button", productName);
        click(By.xpath(formatButtonLocator));
    }

    public int getCartBadgeCount() {
        if (isDisplayed(shoppingCartBadge)) {
            return Integer.parseInt(getText(shoppingCartBadge));
        }
        return 0;
    }

    public CartPage clickCart() {
        click(shoppingCartLink);
        return new CartPage(driver);
    }
}
