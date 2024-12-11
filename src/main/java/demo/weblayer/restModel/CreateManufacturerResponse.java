package demo.weblayer.restModel;

public class CreateManufacturerResponse {
    private String manufacturerId;

    private CreateManufacturerResponse() {
        // For Jackson
    }

    public CreateManufacturerResponse(String manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public String getManufacturerId() {
        return manufacturerId;
    }
}
