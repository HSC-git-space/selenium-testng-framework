package tests;
import org.openqa.selenium.chrome.ChromeOptions;

import com.aventstack.extentreports.Status;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import utils.ExtentReportManager;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class BaseTest {

    protected WebDriver driver;

    @BeforeMethod
    public void setUp(java.lang.reflect.Method method) {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        ExtentReportManager.startTest(method.getName());
        ExtentReportManager.getTest().log(Status.INFO, "Browser launched");
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        // Log pass or fail
        if (result.getStatus() == ITestResult.FAILURE) {
            // Take screenshot on failure
            try {
                TakesScreenshot ts = (TakesScreenshot) driver;
                byte[] screenshot = ts.getScreenshotAs(OutputType.BYTES);
                String base64 = Base64.getEncoder().encodeToString(screenshot);
                ExtentReportManager.getTest()
                        .fail(result.getThrowable())
                        .addScreenCaptureFromBase64String(base64, "Failure Screenshot");
            } catch (Exception e) {
                ExtentReportManager.getTest().fail("Screenshot failed: " + e.getMessage());
            }
        } else {
            ExtentReportManager.getTest().log(Status.PASS, "Test passed");
        }

        if (driver != null) {
            driver.quit();
            ExtentReportManager.getTest().log(Status.INFO, "Browser closed");
        }
    }

    @AfterSuite
    public void tearDownSuite() {
        // Write report to disk
        ExtentReportManager.flushReports();
        System.out.println("Report generated at: /reports/TestReport.html");
    }
}