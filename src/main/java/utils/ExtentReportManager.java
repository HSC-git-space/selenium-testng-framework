package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentReportManager {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    // Creates the report file
    public static ExtentReports getInstance() {
        if (extent == null) {
            ExtentSparkReporter reporter = new ExtentSparkReporter(
                    System.getProperty("user.dir") + "/reports/TestReport.html"
            );
            reporter.config().setTheme(Theme.DARK);
            reporter.config().setDocumentTitle("Login Test Report");
            reporter.config().setReportName("Selenium TestNG Framework");

            extent = new ExtentReports();
            extent.attachReporter(reporter);
            extent.setSystemInfo("Tester", "Harsh");
            extent.setSystemInfo("Environment", "QA");
            extent.setSystemInfo("Browser", "Chrome");
        }
        return extent;
    }

    // Start logging a test
    public static void startTest(String testName) {
        ExtentTest extentTest = getInstance().createTest(testName);
        test.set(extentTest);
    }

    // Get current test logger
    public static ExtentTest getTest() {
        return test.get();
    }

    // Flush writes report to disk
    public static void flushReports() {
        if (extent != null) {
            extent.flush();
        }
    }
}