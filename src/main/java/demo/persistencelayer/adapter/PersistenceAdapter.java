package demo.persistencelayer.adapter;

import demo.persistencelayer.model.Beer;
import demo.persistencelayer.model.Manufacturer;
import demo.persistencelayer.model.User;
import demo.servicelayer.model.users.ManufacturerUser;
import org.springframework.stereotype.Component;

@Component
public class PersistenceAdapter {
    public User mapUser(demo.servicelayer.model.users.User serviceUser) {
        String manufacturerUserId = null;
        if (serviceUser instanceof ManufacturerUser) {
            manufacturerUserId = ((ManufacturerUser) serviceUser).getManufacturerId();
        }

        return new User(serviceUser.getUserName(), serviceUser.getUserPassword(), serviceUser.isAdmin(), manufacturerUserId);
    }

    public demo.servicelayer.model.users.User mapFromUser(User persistenceUser) {
        if (persistenceUser.getManufacturerId() != null) {
            return new ManufacturerUser(persistenceUser.getUserName(), persistenceUser.getUserPassword(), persistenceUser.getManufacturerId());
        }

        return new demo.servicelayer.model.users.User(persistenceUser.getUserName(), persistenceUser.getUserPassword(), persistenceUser.isAdmin());
    }

    public Beer mapBeer(demo.servicelayer.model.Beer serviceBeer) {
        return new Beer(serviceBeer.getId(),
                serviceBeer.getName(),
                serviceBeer.getGraduation(),
                serviceBeer.getType(),
                serviceBeer.getDescription(),
                serviceBeer.getManufacturerName(),
                serviceBeer.getManufacturerId());
    }

    public demo.servicelayer.model.Beer mapFromBeer(Beer persistenceBeer) {
        return new demo.servicelayer.model.Beer(persistenceBeer.getId(),
                persistenceBeer.getName(),
                persistenceBeer.getGraduation(),
                persistenceBeer.getType(),
                persistenceBeer.getDescription(),
                persistenceBeer.getManufacturerName(),
                persistenceBeer.getManufacturerId());
    }

    public Manufacturer mapManufacturer(demo.servicelayer.model.Manufacturer serviceManufacturer) {
        return new Manufacturer(serviceManufacturer.getId(),
                serviceManufacturer.getName(),
                serviceManufacturer.getNationality());
    }

    public demo.servicelayer.model.Manufacturer mapFromManufacturer(Manufacturer persistenceManufacturer) {
        return new demo.servicelayer.model.Manufacturer(persistenceManufacturer.getId(),
                persistenceManufacturer.getName(),
                persistenceManufacturer.getNationality());
    }
}
