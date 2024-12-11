package demo.weblayer.restModel;

public class LoginResponse {
    private String userToken;

    private LoginResponse() {
        // For Jackson
    }

    public LoginResponse(String userToken) {
        this.userToken = userToken;
    }

    public String getUserToken() {
        return userToken;
    }
}
