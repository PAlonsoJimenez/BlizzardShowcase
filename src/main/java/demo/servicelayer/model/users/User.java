package demo.servicelayer.model.users;

public class User {
    private final String userName;
    private final String userPassword;
    private final boolean isAdmin;

    public User (String userName, String userPassword, boolean isAdmin) {
        this.userName = userName;
        this.userPassword = userPassword;
        this.isAdmin = isAdmin;
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
}
