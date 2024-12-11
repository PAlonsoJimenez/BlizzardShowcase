package demo.weblayer;

import demo.servicelayer.CatalogueService;
import demo.servicelayer.exceptions.CatalogueException;
import demo.servicelayer.exceptions.UserException;
import demo.servicelayer.security.UserDetailsImpl;
import demo.weblayer.adapter.ServiceAdapter;
import demo.weblayer.restModel.Beer;
import demo.weblayer.restModel.CreateBeerRequest;
import demo.weblayer.restModel.CreateManufacturerRequest;
import demo.weblayer.restModel.CreateManufacturerResponse;
import demo.weblayer.restModel.ListBeersResponse;
import demo.weblayer.restModel.ListManufacturersResponse;
import demo.weblayer.restModel.Manufacturer;
import demo.weblayer.restModel.UpdateBeerRequest;
import demo.weblayer.restModel.UpdateManufacturerRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CatalogueWebController {
    private static final String ROOT_PATH = "/catalogue";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";

    private final PayloadValidator payloadValidator;
    private final ServiceAdapter serviceAdapter;
    private final CatalogueService catalogueService;

    @Autowired
    public CatalogueWebController(PayloadValidator payloadValidator,
                                  ServiceAdapter serviceAdapter,
                                  CatalogueService catalogueService) {
        this.payloadValidator = payloadValidator;
        this.serviceAdapter = serviceAdapter;
        this.catalogueService = catalogueService;
    }

    /**
     * GET /catalogue/beers
     * List all beers available
     *
     * @return The list of all beers available. (status code 200)
     */
    @GetMapping(
            value = ROOT_PATH + "/beers",
            produces = {"application/json"})
    public ResponseEntity<ListBeersResponse> listBeers() {
        List<Beer> beerList = catalogueService.getAllBeerList().stream().map(serviceAdapter::mapFromBeer).toList();
        return new ResponseEntity<>(new ListBeersResponse(beerList), HttpStatus.OK);
    }

    /**
     * POST /catalogue/createbeer
     * Create a new Beer under a manufacturer
     *
     * @return The new Beer has been created successfully. (status code 201)
     * or The Beer creation has failed due to invalid input. (status code 400)
     * or The Beer creation has failed due to lack of credentials (status code 401)
     */
    @PostMapping(
            value = ROOT_PATH + "/createbeer",
            consumes = {"application/json"})
    public ResponseEntity<String> createBeer(
            @RequestBody CreateBeerRequest createBeerRequest,
            @RequestParam(required = false) String manufacturerId) {

        if (!payloadValidator.validateCreateBeerRequest(createBeerRequest))
            throw new CatalogueException(CatalogueException.CatalogueExceptionTypes.INVALID_INPUT, "Invalid CreateBeerRequest payload");

        String beerManufacturerId;
        if (isAuthenticatedUserAdmin()) {
            if (manufacturerId == null || manufacturerId.isBlank())
                throw new CatalogueException(CatalogueException.CatalogueExceptionTypes.INVALID_INPUT, "ManufacturerId is empty or null. A Beer must belong to a manufacturer");
            beerManufacturerId = manufacturerId;
        } else {
            // We assume that the caller is a Manufacturer user if it's authenticated and not an admin.
            String authenticatedUserManufacturerId = getAuthenticatedUserManufacturerId();
            if (authenticatedUserManufacturerId == null)
                throw new CatalogueException(CatalogueException.CatalogueExceptionTypes.BAD_AUTHORIZATION, "The user logged is not a Manufacturer nor an admin");
            beerManufacturerId = authenticatedUserManufacturerId;
        }

        catalogueService.createNewBeer(serviceAdapter.mapBeer(createBeerRequest.getBeer()), beerManufacturerId);
        return new ResponseEntity<>("ok", HttpStatus.CREATED);
    }

    /**
     * GET /catalogue/beers/{beerId}
     * Get data from a specific beer
     *
     * @return The data from the beer with that id (status code 200)
     * or The Beer retrieval has failed due to invalid input. (status code 400)
     * or No beer with that id has been found. (status code 404)
     */
    @GetMapping(
            value = ROOT_PATH + "/beers/{beerId}",
            produces = {"application/json"})
    public ResponseEntity<Beer> getBeer(@PathVariable("beerId") String beerId) {
        if (beerId == null || beerId.isBlank()) {
            throw new CatalogueException(CatalogueException.CatalogueExceptionTypes.INVALID_INPUT, "Missing BeerId path parameter");
        }

        Beer response = serviceAdapter.mapFromBeer(catalogueService.getBeer(beerId));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * PATCH /catalogue/updatebeer
     * Update data from a specific beer
     *
     * @return The beer data has been updated successfully (status code 200)
     * or The Beer data update has failed due to invalid input. (status code 400)
     * or The authorization has failed. (status code 403)
     * or No beer with that id has been found. (status code 404)
     */
    @PatchMapping(
            value = ROOT_PATH + "/updatebeer",
            produces = {"application/json"},
            consumes = {"application/json"})

    public ResponseEntity<String> updateBeer(@RequestBody UpdateBeerRequest updateBeerRequest) {

        if (!payloadValidator.validateUpdateBeerRequest(updateBeerRequest))
            throw new CatalogueException(CatalogueException.CatalogueExceptionTypes.INVALID_INPUT, "Invalid UpdateBeerRequest payload");

        // If the caller is an admin, no need to check for authorization
        if (!isAuthenticatedUserAdmin()) {
            // We assume that the caller is a Manufacturer user if it's authenticated and not an admin.
            // We check if the caller is in fact the manufacturer of that beer
            String authUserManufacturerId = getAuthenticatedUserManufacturerId();
            if (!updateBeerRequest.getBeer().getManufacturerId().equals(authUserManufacturerId)) {
                throw new UserException(UserException.UserExceptionTypes.FORBIDDEN);
            }
        }

        catalogueService.updateBeerData(serviceAdapter.mapBeer(updateBeerRequest.getBeer()));
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

    /**
     * GET /catalogue/manufacturers
     * List all manufacturers available
     *
     * @return The list of all manufacturers available. (status code 200)
     */
    @GetMapping(
            value = ROOT_PATH + "/manufacturers",
            produces = {"application/json"})

    public ResponseEntity<ListManufacturersResponse> listManufacturers() {
        List<Manufacturer> manufacturerList =
                catalogueService.getAllManufacturerList()
                        .stream()
                        .map(serviceAdapter::mapFromManufacturer)
                        .toList();

        return new ResponseEntity<>(new ListManufacturersResponse(manufacturerList), HttpStatus.OK);
    }

    /**
     * POST /catalogue/createmanufacturer
     * Create a new Manufacturer. Only admin users will be able to create
     * a new manufacturer using this endpoint.
     *
     * @return The new Manufacturer id that has been created successfully. (status code 201)
     * or The Manufacturer creation has failed due to invalid input. (status code 400)
     * or The authorization has failed. (status code 403)
     */
    @PostMapping(
            value = ROOT_PATH + "/createmanufacturer",
            consumes = {"application/json"})
    public ResponseEntity<CreateManufacturerResponse> createManufacturer(
            @RequestBody CreateManufacturerRequest createManufacturerRequest) {

        if (!payloadValidator.validateCreateManufacturerRequest(createManufacturerRequest))
            throw new CatalogueException(CatalogueException.CatalogueExceptionTypes.INVALID_INPUT, "Invalid CreateManufacturerRequest payload");

        String manufacturerId = catalogueService.createNewManufacturer(serviceAdapter.mapManufacturer(createManufacturerRequest.getManufacturer()));
        return new ResponseEntity<>(new CreateManufacturerResponse(manufacturerId), HttpStatus.CREATED);
    }

    /**
     * GET /catalogue/manufacturers/{manufacturerId}
     * Get data from a specific manufacturer
     *
     * @return The data from the manufacturer with that id (status code 200)
     * or The manufacturer retrieval has failed due to invalid input. (status code 400)
     * or No manufacturer with that id has been found. (status code 404)
     */
    @GetMapping(
            value = ROOT_PATH + "/manufacturers/{manufacturerId}",
            produces = {"application/json"})
    public ResponseEntity<Manufacturer> getManufacturer(@PathVariable("manufacturerId") String manufacturerId) {

        if (manufacturerId == null || manufacturerId.isBlank()) {
            throw new CatalogueException(CatalogueException.CatalogueExceptionTypes.INVALID_INPUT, "Missing manufacturerId path parameter");
        }

        Manufacturer response = serviceAdapter.mapFromManufacturer(catalogueService.getManufacturer(manufacturerId));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * PATCH /catalogue/updatemanufacturer
     * Update data from a specific manufacturer
     *
     * @return The manufacturer data has been updated successfully (status code 200)
     * or The manufacturer data update has failed due to invalid input. (status code 400)
     * or The authorization has failed. (status code 403)
     * or No manufacturer with that id has been found. (status code 404)
     */
    @PatchMapping(
            value = ROOT_PATH + "/updatemanufacturer",
            produces = {"application/json"},
            consumes = {"application/json"})

    public ResponseEntity<String> updateManufacturer(@RequestBody UpdateManufacturerRequest updateManufacturerRequest) {

        if (!payloadValidator.validateUpdateManufacturerRequest(updateManufacturerRequest))
            throw new CatalogueException(CatalogueException.CatalogueExceptionTypes.INVALID_INPUT, "Invalid UpdateManufacturerRequest payload");

        // If the caller is an admin, no need to check for authorization
        if (!isAuthenticatedUserAdmin()) {
            // We assume that the caller is a Manufacturer user if it's authenticated and not an admin.
            // We check if the caller is in fact the manufacturer User responsible for that manufacturer
            String authUserManufacturerId = getAuthenticatedUserManufacturerId();
            if (!updateManufacturerRequest.getManufacturer().getId().equals(authUserManufacturerId)) {
                throw new UserException(UserException.UserExceptionTypes.FORBIDDEN);
            }
        }

        catalogueService.updateManufacturerData(
                serviceAdapter.mapManufacturer(updateManufacturerRequest.getManufacturer()));

        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

    private boolean isAuthenticatedUserAdmin() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        return userDetails.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals(ROLE_ADMIN));
    }

    private String getAuthenticatedUserManufacturerId() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return userDetails.getManufacturerId();
    }
}
