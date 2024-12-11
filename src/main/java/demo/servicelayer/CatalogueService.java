package demo.servicelayer;

import demo.persistencelayer.DatabaseManager;
import demo.servicelayer.exceptions.CatalogueException;
import demo.servicelayer.model.Beer;
import demo.servicelayer.model.Manufacturer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CatalogueService {
    private final DatabaseManager databaseManager;

    @Autowired
    public CatalogueService(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    /**
     * Get a list with all beers in our system
     *
     * @return a list with all beers stored in our system
     */
    public List<Beer> getAllBeerList() {
        return databaseManager.getAllBeers();
    }

    /**
     * Create a new beer in our system if and only if
     * the manufacturer exist and no beer with the same
     * name as this one exist under that manufacturer
     * @param beer the new beer to create
     * @param manufacturerId the manufacturer owner of that beer
     * @throws CatalogueException if no manufacturer with that id is found
     * or if a Beer with that name under that manufacturer already exist
     */
    public void createNewBeer(Beer beer, String manufacturerId) {
        Optional<Manufacturer> optionalBeerManufacturer = databaseManager.getManufacturer(manufacturerId);

        if (optionalBeerManufacturer.isEmpty())
            throw new CatalogueException(CatalogueException.CatalogueExceptionTypes.MANUFACTURER_NOT_FOUND, "No manufacturer with that id was found");

        Manufacturer beerManufacturer = optionalBeerManufacturer.get();

        Beer newBeerToBeStored = new Beer(beer.getId(),
                beer.getName(),
                beer.getGraduation(),
                beer.getType(),
                beer.getDescription(),
                beerManufacturer.getName(),
                manufacturerId);

        boolean created = databaseManager.storeBeer(newBeerToBeStored);
        if (!created)
            throw new CatalogueException(CatalogueException.CatalogueExceptionTypes.INVALID_INPUT,
                    "A Beer with that name under that manufacturer already exist");
    }

    /**
     * Get Beer by id
     * @param beerId the beerId
     * @return the Beer with that specific id
     * @throws CatalogueException When no beer with that id is found
     */
    public Beer getBeer(String beerId) {
        Optional<Beer> optionalBeer = databaseManager.getBeer(beerId);

        if (optionalBeer.isEmpty()) {
            throw new CatalogueException(CatalogueException.CatalogueExceptionTypes.BEER_NOT_FOUND, "No Beer with that Id was found");
        } else {
            return optionalBeer.get();
        }
    }

    /**
     * Update a specific beer data
     * @param beer the beer with its updated data
     * @throws CatalogueException if no Beer with that id is found,
     * or the name already exist under the same manufacturer
     */
    public void updateBeerData(Beer beer) {
        boolean updatedSuccessfully = databaseManager.updateBeerData(beer);
        if (!updatedSuccessfully) {
            throw new CatalogueException(CatalogueException.CatalogueExceptionTypes.INVALID_INPUT,
                    "No Beer with that Id was found, or the new name already exist under the same Manufacturer");
        }
    }

    /**
     * Get a list with all manufacturers in our system
     *
     * @return a list with all manufacturers stored in our system
     */
    public List<Manufacturer> getAllManufacturerList() {
        return databaseManager.getAllManufacturers();
    }

    /**
     * Create a new manufacturer in our system
     * @param manufacturer the manufacturer to be created
     * @return the created manufacturer Id if the manufacturer was successfully created
     * @throws CatalogueException if the manufacturer with that name already exist
     */
    public String createNewManufacturer(Manufacturer manufacturer) {
        Optional<String> optionalManufacturerId = databaseManager.storeManufacturer(manufacturer);
        if (optionalManufacturerId.isEmpty()) {
            throw new CatalogueException(CatalogueException.CatalogueExceptionTypes.INVALID_INPUT, "A Manufacturer with that name already exist");
        }

        return optionalManufacturerId.get();
    }

    /**
     * Get Manufacturer by id
     * @param manufacturerId the manufacturerId
     * @return the Manufacturer with that specific id
     * @throws CatalogueException When no Manufacturer with that id is found
     */
    public Manufacturer getManufacturer(String manufacturerId) {
        Optional<Manufacturer> optionalBeerManufacturer = databaseManager.getManufacturer(manufacturerId);

        if (optionalBeerManufacturer.isEmpty())
            throw new CatalogueException(CatalogueException.CatalogueExceptionTypes.MANUFACTURER_NOT_FOUND, "No manufacturer with that id was found");

        return optionalBeerManufacturer.get();
    }

    /**
     * Update a specific Manufacturer data
     * @param manufacturer the manufacturer with its updated data
     * @throws CatalogueException if no Manufacturer with that id is found,
     * or a Manufacturer with the new name already exist
     */
    public void updateManufacturerData(Manufacturer manufacturer) {
        boolean updatedSuccessfully = databaseManager.updateManufacturer(manufacturer);
        if (!updatedSuccessfully) {
            throw new CatalogueException(CatalogueException.CatalogueExceptionTypes.INVALID_INPUT,
                    "No Manufacturer with that Id was found, or a Manufacturer with the new name already exist in our database");
        }
    }
}
