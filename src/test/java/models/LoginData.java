package models;

public class LoginData {

    public String username;
    public String password;
    public String expectedResult;

    public LoginData(String username, String password, String expectedResult) {
        this.username = username;
        this.password = password;
        this.expectedResult = expectedResult;
    }
}