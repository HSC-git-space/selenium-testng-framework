package tests;

import dataproviders.LoginDataProvider;
import models.LoginData;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;

public class LoginTest extends BaseTest {

    @Test(dataProvider = "loginData",
            dataProviderClass = LoginDataProvider.class,
            groups = {"smoke", "regression"})
    public void testLogin(LoginData testData) {
        
        driver.get("https://practicetestautomation.com/practice-test-login/");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(testData.username, testData.password);

        if (testData.expectedResult.equalsIgnoreCase("success")) {
            Assert.assertTrue(
                    loginPage.isLoginSuccessful(),
                    "Expected success but failed for: " + testData.username
            );
        } else {
            Assert.assertFalse(
                    loginPage.isLoginSuccessful(),
                    "Expected failure but passed for: " + testData.username
            );
        }
    }
}