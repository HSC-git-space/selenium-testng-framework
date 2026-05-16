package base;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;
    private static final int MAX_STALE_RETRY = 3;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    protected WebElement waitForVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected void waitForInvisible(By locator) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    protected void sendKeys(By locator, String text) {
        int attempts = 0;
        while (attempts < MAX_STALE_RETRY) {
            try {
                WebElement element = waitForVisible(locator);
                element.clear();
                element.sendKeys(text);
                return;
            } catch (StaleElementReferenceException e) {
                attempts++;
                if (attempts == MAX_STALE_RETRY) {
                    throw new RuntimeException("Element still stale after " + MAX_STALE_RETRY + " attempts: " + locator, e);
                }
            }
        }
    }

    protected void click(By locator) {
        int attempts = 0;
        while (attempts < MAX_STALE_RETRY) {
            try {
                waitForClickable(locator).click();
                return;
            } catch (StaleElementReferenceException e) {
                attempts++;
                if (attempts == MAX_STALE_RETRY) {
                    throw new RuntimeException("Element still stale after " + MAX_STALE_RETRY + " attempts: " + locator, e);
                }
            }
        }
    }

    protected String getText(By locator) {
        int attempts = 0;
        while (attempts < MAX_STALE_RETRY) {
            try {
                return waitForVisible(locator).getText();
            } catch (StaleElementReferenceException e) {
                attempts++;
                if (attempts == MAX_STALE_RETRY) {
                    throw new RuntimeException("Element still stale after " + MAX_STALE_RETRY + " attempts: " + locator, e);
                }
            }
        }
        return null;
    }

    protected boolean isDisplayed(By locator) {
        try {
            return waitForVisible(locator).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }
}