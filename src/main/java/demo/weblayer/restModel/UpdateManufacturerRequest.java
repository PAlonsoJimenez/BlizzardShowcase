package demo.weblayer.restModel;

public class UpdateManufacturerRequest {
    private Manufacturer manufacturer;

    private UpdateManufacturerRequest() {
        // For Jackson
    }

    public UpdateManufacturerRequest(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }
}
