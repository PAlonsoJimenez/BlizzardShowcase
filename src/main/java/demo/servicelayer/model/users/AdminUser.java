package demo.servicelayer.model.users;

public class AdminUser extends User {

    public AdminUser(String userName, String userPassword) {
        super(userName, userPassword, true);
    }
}
