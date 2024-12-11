package demo.weblayer.restModel;

public class CreateManufacturerRequest {
    private Manufacturer manufacturer;

    private CreateManufacturerRequest() {
        // For Jackson
    }

    public CreateManufacturerRequest(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }
}
