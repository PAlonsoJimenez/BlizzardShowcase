package demo.persistencelayer.model;

public class User {
    private final String userName;
    private final String userPassword;
    private final boolean isAdmin;
    private final String manufacturerId;

    public User(String userName, String userPassword, boolean isAdmin, String manufacturerId) {
        this.userName = userName;
        this.userPassword = userPassword;
        this.isAdmin = isAdmin;
        this.manufacturerId = manufacturerId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public String getManufacturerId() {
        return manufacturerId;
    }
}
