package com.datashield.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class OrderConfirmationPage extends BasePage {

    private final By completeHeader = By.className("complete-header");
    private final By completeText = By.className("complete-text");

    public OrderConfirmationPage(WebDriver driver) {
        super(driver);
    }

    public boolean isOrderComplete() {
        return isDisplayed(completeHeader) && getText(completeHeader).equalsIgnoreCase("Thank you for your order!");
    }

    public String getConfirmationMessage() {
        return getText(completeText);
    }
}
