package dataproviders;

import models.LoginData;
import org.testng.annotations.DataProvider;
import utils.ExcelUtils;

public class LoginDataProvider {

    private static final String FILE_PATH =
            System.getProperty("user.dir")
                    + "/src/test/resources/testdata/LoginData.xlsx";

    @DataProvider(name = "loginData")
    public static Object[][] getLoginData() throws Exception {
        ExcelUtils excel = new ExcelUtils(FILE_PATH, "Sheet1");
        int rows = excel.getRowCount();

        Object[][] data = new Object[rows][1];

        for (int i = 0; i < rows; i++) {
            String username       = excel.getCellData(i + 1, 0);
            String password       = excel.getCellData(i + 1, 1);
            String expectedResult = excel.getCellData(i + 1, 2);

            data[i][0] = new LoginData(username, password, expectedResult);
        }

        excel.close();
        return data;
    }
}