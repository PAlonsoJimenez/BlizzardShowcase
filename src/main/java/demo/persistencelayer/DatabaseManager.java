package demo.persistencelayer;

import demo.persistencelayer.adapter.PersistenceAdapter;
import demo.persistencelayer.database.DatabaseTalker;
import demo.persistencelayer.database.InMemoryDatabase;
import demo.servicelayer.model.Beer;
import demo.servicelayer.model.Manufacturer;
import demo.servicelayer.model.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class DatabaseManager {
    private final PersistenceAdapter persistenceAdapter;
    private final DatabaseTalker databaseTalker;

    @Autowired
    public DatabaseManager(PersistenceAdapter persistenceAdapter) {
        this.persistenceAdapter = persistenceAdapter;
        databaseTalker = InMemoryDatabase.getInstance();
    }

    public boolean addUser(User newUser) {
        return databaseTalker.addUser(persistenceAdapter.mapUser(newUser));
    }

    public Optional<User> getUserByEmail(String userEmail) {
        Optional<demo.persistencelayer.model.User> persistenceOptionalUser = databaseTalker.getUserByEmail(userEmail);
        return persistenceOptionalUser.map(persistenceAdapter::mapFromUser);
    }

    public boolean storeBeer(Beer newBeer) {
        return databaseTalker.storeBeer(persistenceAdapter.mapBeer(newBeer));
    }

    public boolean updateBeerData(Beer newBeerData){
        return databaseTalker.updateBeerData(persistenceAdapter.mapBeer(newBeerData));
    }

    public void deleteBeer(String beerId) {
        databaseTalker.deleteBeer(beerId);
    }

    public Optional<Beer> getBeer(String beerId) {
        Optional<demo.persistencelayer.model.Beer> persistenceOptionalBeer = databaseTalker.getBeer(beerId);
        return persistenceOptionalBeer.map(persistenceAdapter::mapFromBeer);
    }

    public List<Beer> getAllBeers() {
        return databaseTalker.getAllBeers()
                .stream()
                .map(persistenceAdapter::mapFromBeer)
                .collect(Collectors.toList());
    }

    public List<Beer> getAllBeerFromManufacturer(String manufacturerId) {
        return databaseTalker.getAllBeerFromManufacturer(manufacturerId)
                .stream()
                .map(persistenceAdapter::mapFromBeer)
                .collect(Collectors.toList());
    }

    public Optional<String> storeManufacturer(Manufacturer newManufacturer) {
        return databaseTalker.storeManufacturer(persistenceAdapter.mapManufacturer(newManufacturer));
    }

    public boolean updateManufacturer(Manufacturer newManufacturerData){
        return databaseTalker.updateManufacturer(persistenceAdapter.mapManufacturer(newManufacturerData));
    }

    public void deleteManufacturer(String manufacturerId) {
        databaseTalker.deleteManufacturer(manufacturerId);
    }

    public Optional<Manufacturer> getManufacturer(String manufacturerId) {
        Optional<demo.persistencelayer.model.Manufacturer> persistenceOptionalManufacturer = databaseTalker.getManufacturer(manufacturerId);
        return persistenceOptionalManufacturer.map(persistenceAdapter::mapFromManufacturer);
    }

    public List<Manufacturer> getAllManufacturers() {
        return databaseTalker.getAllManufacturers()
                .stream()
                .map(persistenceAdapter::mapFromManufacturer)
                .collect(Collectors.toList());
    }
}
