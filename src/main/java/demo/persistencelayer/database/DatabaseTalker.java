package demo.persistencelayer.database;

import demo.persistencelayer.model.Beer;
import demo.persistencelayer.model.Manufacturer;
import demo.persistencelayer.model.User;

import java.util.List;
import java.util.Optional;

public interface DatabaseTalker {

    /**
     * Add a new User to the database
     * @param newUser the user to be added
     * @return true if the user was successfully added, or
     * false if the user already existed in our database.
     */
    boolean addUser(User newUser);

    /**
     * Get user by email
     * @param userEmail the userEmail
     * @return the User with that specific email, or
     * null if no User with that email was found in the
     * database
     */
    Optional<User> getUserByEmail(String userEmail);

    /**
     * Store a new Beer to the database
     * @param newBeer the beer to be added
     * @return true if the beer was successfully added, or
     * false if its manufactured does not exist or
     * a beer with that name already exist under
     * its specific manufacturer
     */
    boolean storeBeer(Beer newBeer);

    /**
     * Update a specific beer data
     * @param newBeerData the beer with its updated data
     * @return true if the beer was successfully updated, or
     * false if a beer with that name already exist under
     * its specific manufacturer, or if no beer with that
     * beer id exist in our database.
     */
    boolean updateBeerData(Beer newBeerData);

    /**
     * Delete the Beer with that specific
     * id from our database
     * @param beerId the beer id
     */
    void deleteBeer(String beerId);

    /**
     * Get Beer by id
     * @param beerId the beerId
     * @return An Optional with the Beer with that specific id, or
     * an empty Optional if no Beer with that id was found in the
     * database
     */
    Optional<Beer> getBeer(String beerId);

    /**
     * Get a list of all Beers in our database
     * @return a list containing all the Beer in our
     * database
     */
    List<Beer> getAllBeers();

    /**
     * Get a list of all Beers in our database from a
     * specific manufacturer
     * @param manufacturerId the specific manufacturer id
     * @return a list with all Beer from a specific manufacturer
     * in our database.
     */
    List<Beer> getAllBeerFromManufacturer(String manufacturerId);

    /**
     * Store a new Manufacturer in the database
     * @param newManufacturer the manufacturer to be stored
     * @return An Optional with the manufacturer id if it was successfully stored,
     * or an empty Optional if a manufacturer with that name already
     * existed in our database.
     */
    Optional<String> storeManufacturer(Manufacturer newManufacturer);

    /**
     * Update a specific Manufacturer data
     * @param newManufacturerData the manufacturer with its updated data
     * @return true if the manufacturer was successfully updated, or
     * false if a manufacturer with that name already exist, or
     * if no manufacturer with that manufacturer id exist in our database.
     */
    boolean updateManufacturer(Manufacturer newManufacturerData);

    /**
     * Delete the Manufacturer with that specific
     * id from our database
     * @param manufacturerId the manufacturer id
     */
    void deleteManufacturer(String manufacturerId);

    /**
     * Get Manufacturer by id
     * @param manufacturerId the manufacturerId
     * @return An Optional with the Manufacturer with that specific id, or
     * an empty optional if no Manufacturer with that id was found in the
     * database
     */
    Optional<Manufacturer> getManufacturer(String manufacturerId);

    /**
     * Get a list of all Manufacturers in our database
     * @return a list containing all the Manufacturer in our
     * database
     */
    List<Manufacturer> getAllManufacturers();

}
