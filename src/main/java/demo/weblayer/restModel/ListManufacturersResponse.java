package demo.weblayer.restModel;

import java.util.List;

public class ListManufacturersResponse {
    private List<Manufacturer> manufacturerList;

    private ListManufacturersResponse() {
        // For Jackson
    }

    public ListManufacturersResponse(List<Manufacturer> manufacturerList) {
        this.manufacturerList = manufacturerList;
    }

    public List<Manufacturer> getManufacturerList() {
        return manufacturerList;
    }
}
