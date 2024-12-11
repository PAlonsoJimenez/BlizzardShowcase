package demo.weblayer.restModel;

public class CreateUserRequest {
    private String userEmail;
    private String password;
    private boolean admin;
    private Manufacturer manufacturer; // Optional

    private CreateUserRequest() {
        // For Jackson
    }

    public CreateUserRequest(String userEmail, String password, boolean admin, Manufacturer manufacturer) {
        this.userEmail = userEmail;
        this.password = password;
        this.admin = admin;
        this.manufacturer = manufacturer;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAdmin() {
        return admin;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }
}
