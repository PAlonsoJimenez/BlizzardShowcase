package demo.weblayer.restModel;

public class LoginRequest {
    private String userEmail;
    private String password;

    private LoginRequest() {
        // For Jackson
    }

    public LoginRequest(String userEmail, String password) {
        this.userEmail = userEmail;
        this.password = password;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getPassword() {
        return password;
    }
}
