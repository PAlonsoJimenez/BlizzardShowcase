package demo.servicelayer.model.users;

public class ManufacturerUser extends User {
    private final String manufacturerId;

    public ManufacturerUser (String userName, String userPassword, String manufacturerId) {
        super(userName, userPassword, false);
        this.manufacturerId = manufacturerId;
    }

    public String getManufacturerId() {
        return manufacturerId;
    }
}
