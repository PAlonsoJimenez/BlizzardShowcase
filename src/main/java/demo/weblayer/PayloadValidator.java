package demo.weblayer;

import demo.servicelayer.exceptions.CatalogueException;
import demo.servicelayer.exceptions.UserException;
import demo.weblayer.restModel.Beer;
import demo.weblayer.restModel.CreateBeerRequest;
import demo.weblayer.restModel.CreateManufacturerRequest;
import demo.weblayer.restModel.CreateUserRequest;
import demo.weblayer.restModel.LoginRequest;
import demo.weblayer.restModel.Manufacturer;
import demo.weblayer.restModel.UpdateBeerRequest;
import demo.weblayer.restModel.UpdateManufacturerRequest;
import org.springframework.stereotype.Component;

@Component
public class PayloadValidator {
    // Usually this is done with annotations in the rest model classes

    public boolean validateCreateBeerRequest(CreateBeerRequest createBeerRequest) {
        if (createBeerRequest == null)
            throw new CatalogueException(CatalogueException.CatalogueExceptionTypes.INVALID_INPUT, "Missing CreateBeerRequest payload");

        Beer beerPayload = createBeerRequest.getBeer();
        if (beerPayload == null)
            throw new CatalogueException(CatalogueException.CatalogueExceptionTypes.INVALID_INPUT, "CreateBeerRequest cannot have a null Beer");

        return validateBeerPayload(beerPayload);
    }

    public boolean validateUpdateBeerRequest(UpdateBeerRequest updateBeerRequest) {
        if (updateBeerRequest == null)
            throw new CatalogueException(CatalogueException.CatalogueExceptionTypes.INVALID_INPUT, "Missing UpdateBeerRequest payload");

        Beer beerPayload = updateBeerRequest.getBeer();
        if (beerPayload == null)
            throw new CatalogueException(CatalogueException.CatalogueExceptionTypes.INVALID_INPUT, "UpdateBeerRequest cannot have a null Beer");

        return validateBeerPayload(beerPayload);
    }

    public boolean validateCreateManufacturerRequest(CreateManufacturerRequest createManufacturerRequest) {
        if (createManufacturerRequest == null)
            throw new CatalogueException(CatalogueException.CatalogueExceptionTypes.INVALID_INPUT, "Missing CreateManufacturerRequest payload");

        Manufacturer manufacturerPayload = createManufacturerRequest.getManufacturer();
        if (manufacturerPayload == null)
            throw new CatalogueException(CatalogueException.CatalogueExceptionTypes.INVALID_INPUT, "CreateManufacturerRequest cannot have a null Manufacturer");

        return validateManufacturerPayload(manufacturerPayload);
    }

    public boolean validateUpdateManufacturerRequest(UpdateManufacturerRequest updateManufacturerRequest) {
        if (updateManufacturerRequest == null)
            throw new CatalogueException(CatalogueException.CatalogueExceptionTypes.INVALID_INPUT, "Missing UpdateManufacturerRequest payload");

        Manufacturer manufacturerPayload = updateManufacturerRequest.getManufacturer();
        if (manufacturerPayload == null)
            throw new CatalogueException(CatalogueException.CatalogueExceptionTypes.INVALID_INPUT, "UpdateManufacturerRequest cannot have a null Manufacturer");

        return validateManufacturerPayload(manufacturerPayload);
    }

    public boolean validateCreateUserRequest(CreateUserRequest createUserRequest) {
        if (createUserRequest == null)
            throw new UserException(UserException.UserExceptionTypes.INVALID_INPUT, "Missing CreateUserRequest payload");

        if (createUserRequest.getUserEmail() == null || createUserRequest.getUserEmail().isBlank())
            throw new UserException(UserException.UserExceptionTypes.INVALID_INPUT, "A User must have a not null, not empty email");

        if (createUserRequest.getPassword() == null || createUserRequest.getPassword().isBlank())
            throw new UserException(UserException.UserExceptionTypes.INVALID_INPUT, "A User must have a not null, not empty password");

        if (!createUserRequest.isAdmin()) {
            // If the new user is not an admin, is a manufacturer user, so it must have a manufacturer
            Manufacturer manufacturerPayload = createUserRequest.getManufacturer();
            if (manufacturerPayload == null)
                throw new UserException(UserException.UserExceptionTypes.INVALID_INPUT, "A not admin user must also specify its manufacturer");

            // It will throw CatalogueException for invalid user input...
            validateManufacturerPayload(manufacturerPayload);
        }

        return validateEmail(createUserRequest.getUserEmail());
    }

    public boolean validateLoginRequest(LoginRequest loginRequest) {
        if (loginRequest == null)
            throw new UserException(UserException.UserExceptionTypes.INVALID_INPUT, "Missing LoginRequest payload");

        if (loginRequest.getUserEmail() == null || loginRequest.getUserEmail().isBlank())
            throw new UserException(UserException.UserExceptionTypes.INVALID_INPUT, "A User must have a not null, not empty userEmail");

        if (loginRequest.getPassword() == null || loginRequest.getPassword().isBlank())
            throw new UserException(UserException.UserExceptionTypes.INVALID_INPUT, "A User must have a not null, not empty password");

        return true;
    }

    private boolean validateBeerPayload(Beer beerPayload) {

        if (beerPayload.getName() == null || beerPayload.getName().isBlank())
            throw new CatalogueException(CatalogueException.CatalogueExceptionTypes.INVALID_INPUT, "A Beer must have a not null, not empty name");

        if (beerPayload.getGraduation() < 0)
            throw new CatalogueException(CatalogueException.CatalogueExceptionTypes.INVALID_INPUT, "A Beer must have a not negative graduation");

        if (beerPayload.getType() == null || beerPayload.getType().isBlank())
            throw new CatalogueException(CatalogueException.CatalogueExceptionTypes.INVALID_INPUT, "A Beer must have a not null, not empty type");

        if (beerPayload.getDescription() == null || beerPayload.getDescription().isBlank())
            throw new CatalogueException(CatalogueException.CatalogueExceptionTypes.INVALID_INPUT, "A Beer must have a not null, not empty description");

        return true;
    }

    private boolean validateManufacturerPayload(Manufacturer manufacturerPayload) {

        if (manufacturerPayload.getName() == null || manufacturerPayload.getName().isBlank())
            throw new CatalogueException(CatalogueException.CatalogueExceptionTypes.INVALID_INPUT, "A Manufacturer must have a not null, not empty name");

        if (manufacturerPayload.getNationality() == null || manufacturerPayload.getNationality().isBlank())
            throw new CatalogueException(CatalogueException.CatalogueExceptionTypes.INVALID_INPUT, "A Manufacturer must have a not null, not empty nationality");

        return true;
    }

    private boolean validateEmail(String userEmail) {
        return userEmail.matches("^(.+)@(\\S+)$");
    }
}
