package demo.weblayer.restModel;

public class CreateUserResponse {
    private String userToken;

    private CreateUserResponse() {
        // For Jackson
    }

    public CreateUserResponse(String userToken) {
        this.userToken = userToken;
    }

    public String getUserToken() {
        return userToken;
    }
}
