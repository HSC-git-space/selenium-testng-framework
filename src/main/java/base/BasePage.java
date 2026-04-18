package base;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;

    // Constructor — every page gets the driver and a default wait
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Find element — waits up to 10 seconds before giving up
    protected WebElement waitForElement(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    // Type into a field
    protected void sendKeys(By locator, String text) {
        waitForElement(locator).clear();
        waitForElement(locator).sendKeys(text);
    }

    // Click anything
    protected void click(By locator) {
        waitForElement(locator).click();
    }

    // Get text from any element
    protected String getText(By locator) {
        return waitForElement(locator).getText();
    }

    // Check if something is visible
    protected boolean isDisplayed(By locator) {
        try {
            return waitForElement(locator).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }
}