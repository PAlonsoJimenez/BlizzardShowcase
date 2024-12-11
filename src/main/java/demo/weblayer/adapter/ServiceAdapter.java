package demo.weblayer.adapter;

import demo.servicelayer.model.Beer;
import demo.servicelayer.model.Manufacturer;
import org.springframework.stereotype.Component;

@Component
public class ServiceAdapter {

    public Manufacturer mapManufacturer(demo.weblayer.restModel.Manufacturer restManufacturer) {
        return new Manufacturer(restManufacturer.getId(), restManufacturer.getName(), restManufacturer.getNationality());
    }

    public demo.weblayer.restModel.Manufacturer mapFromManufacturer(Manufacturer manufacturer) {
        return new demo.weblayer.restModel.Manufacturer(manufacturer.getId(), manufacturer.getName(), manufacturer.getNationality());
    }

    public Beer mapBeer(demo.weblayer.restModel.Beer restBeer) {
        return new Beer(restBeer.getId(),
                restBeer.getName(),
                restBeer.getGraduation(),
                restBeer.getType(),
                restBeer.getDescription(),
                restBeer.getManufacturerName(),
                restBeer.getManufacturerId());
    }

    public demo.weblayer.restModel.Beer mapFromBeer(Beer beer) {
        return new demo.weblayer.restModel.Beer(beer.getId(),
                beer.getName(),
                beer.getGraduation(),
                beer.getType(),
                beer.getDescription(),
                beer.getManufacturerName(),
                beer.getManufacturerId());
    }
}
