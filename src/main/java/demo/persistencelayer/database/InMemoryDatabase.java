package demo.persistencelayer.database;

import demo.persistencelayer.model.Beer;
import demo.persistencelayer.model.Manufacturer;
import demo.persistencelayer.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InMemoryDatabase implements DatabaseTalker {
    // Singleton for the sake of concurrency
    private static InMemoryDatabase instance;
    private final Map<String, User> userList;
    private final Map<String, Manufacturer> manufacturerList;
    private final Map<String, Beer> beerList;

    private InMemoryDatabase(Map<String, User> userList, Map<String, Manufacturer> manufacturerList, Map<String, Beer> beerList) {
        this.userList = userList;
        this.manufacturerList = manufacturerList;
        this.beerList = beerList;
    }

    public static InMemoryDatabase getInstance() {
        if (instance == null) {
            instance = new InMemoryDatabase(new HashMap<>(), new HashMap<>(), new HashMap<>());
        }

        return instance;
    }

    @Override
    public synchronized boolean addUser(User newUser) {
        // If the user already exist, we return false
        if (userList.containsKey(newUser.getUserName())) return false;
        userList.put(newUser.getUserName(), newUser);
        return true;
    }

    @Override
    public synchronized Optional<User> getUserByEmail(String userEmail) {
        User retrievedUser = userList.get(userEmail);
        if (retrievedUser == null) {
            return Optional.empty();
        } else {
            return Optional.of(retrievedUser);
        }
    }

    @Override
    public synchronized boolean storeBeer(Beer newBeer) {
        // The beer manufacturer does not exist, so we don't add the beer
        if (manufacturerList.get(newBeer.getManufacturerId()) == null) return false;

        List<Beer> allBeersFromThisManufacturer = getAllBeerFromManufacturer(newBeer.getManufacturerId());
        for (Beer beer : allBeersFromThisManufacturer) {
            if (beer.getName().equals(newBeer.getName())) return false;
        }

        // No beer with that name from that manufacturer
        newBeer.setId(generateUUID());

        // If the beerId already exist, we return false.
        // This is a safety net, but a logic plot hole, now beer storage may be
        // rejected because of collision instead of a logic reason.
        if (beerList.containsKey(newBeer.getId())) return false;
        beerList.put(newBeer.getId(), newBeer);
        return true;
    }

    @Override
    public synchronized boolean updateBeerData(Beer newBeerData) {
        // This beer does not exist in our database
        if (!beerList.containsKey(newBeerData.getId())) return false;

        for (Beer beer : beerList.values()) {
            if (beer.getName().equals(newBeerData.getName()) &&
                    beer.getManufacturerId().equals(newBeerData.getManufacturerId()) &&
                    !beer.getId().equals(newBeerData.getId()))
                // A beer with this same name exist under the same manufacturer
                return false;
        }

        // If the beer changes manufacturerId, it will also change its manufacturerName
        if (!beerList.get(newBeerData.getId()).getManufacturerId().equals(newBeerData.getManufacturerId())) {
            Manufacturer newManufacturer = manufacturerList.get(newBeerData.getManufacturerId());
            newBeerData.setManufacturerName(newManufacturer.getName());
        }

        beerList.put(newBeerData.getId(), newBeerData);
        return true;
    }

    @Override
    public synchronized void deleteBeer(String beerId) {
        beerList.remove(beerId);
    }

    @Override
    public synchronized Optional<Beer> getBeer(String beerId) {
        Beer retirevedBeer = beerList.get(beerId);
        if (retirevedBeer == null) return Optional.empty();
        return Optional.of(retirevedBeer);
    }

    @Override
    public synchronized List<Beer> getAllBeers() {
        return new ArrayList<>(beerList.values());
    }

    @Override
    public synchronized List<Beer> getAllBeerFromManufacturer(String manufacturerId) {
        ArrayList<Beer> allBeersFromManufacturer = new ArrayList<>();
        for (Beer beer : beerList.values()) {
            if (beer.getManufacturerId().equals(manufacturerId)) allBeersFromManufacturer.add(beer);
        }

        return allBeersFromManufacturer;
    }

    @Override
    public synchronized Optional<String> storeManufacturer(Manufacturer newManufacturer) {
        for (Manufacturer manufacturer : manufacturerList.values()) {
            if (manufacturer.getName().equals(newManufacturer.getName())) return Optional.empty();
        }

        // No manufacturer with that name exist
        newManufacturer.setId(generateUUID());

        // If the manufacturer already exist, we return false
        // This is a safety net, but bad logic, now manufacturer storage may be
        // rejected because of collision instead of a logic reason.
        if (manufacturerList.containsKey(newManufacturer.getId())) return Optional.empty();
        manufacturerList.put(newManufacturer.getId(), newManufacturer);
        return Optional.of(newManufacturer.getId());
    }

    @Override
    public synchronized boolean updateManufacturer(Manufacturer newManufacturerData) {
        // This manufacturer does not exist in our database
        if (!manufacturerList.containsKey(newManufacturerData.getId())) return false;

        for (Manufacturer manufacturer : manufacturerList.values()) {
            if (manufacturer.getName().equals(newManufacturerData.getName()) &&
                    !manufacturer.getId().equals(newManufacturerData.getId()))
                // A manufacturer with that name already exist in our database.
                return false;
        }

        // If one of the manufacturer changes was the name, we must also change it in all its beers
        if (!manufacturerList.get(newManufacturerData.getId()).getName().equals(newManufacturerData.getName())) {
            List<Beer> beers = new ArrayList<>(beerList.values());
            for (Beer beer : beers) {
                if (beer.getManufacturerId().equals(newManufacturerData.getId())) {
                    beer.setManufacturerName(newManufacturerData.getName());
                    beerList.put(beer.getId(), beer);
                }
            }
        }

        manufacturerList.put(newManufacturerData.getId(), newManufacturerData);
        return true;
    }

    @Override
    public synchronized void deleteManufacturer(String manufacturerId) {
        manufacturerList.remove(manufacturerId);
    }

    @Override
    public synchronized Optional<Manufacturer> getManufacturer(String manufacturerId) {
        Manufacturer manufacturer = manufacturerList.get(manufacturerId);
        if (manufacturer == null) {
            return Optional.empty();
        } else {
            return Optional.of(manufacturer);
        }
    }

    @Override
    public synchronized List<Manufacturer> getAllManufacturers() {
        return new ArrayList<>(manufacturerList.values());
    }

    private synchronized String generateUUID() {
        // I'm manually generating the UUID, so every object stored inside all the maps is prompt to collision
        return UUID.randomUUID().toString();
    }
}
