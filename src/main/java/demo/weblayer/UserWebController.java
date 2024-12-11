package demo.weblayer;

import demo.servicelayer.UserService;
import demo.servicelayer.exceptions.UserException;
import demo.weblayer.adapter.ServiceAdapter;
import demo.weblayer.restModel.CreateUserRequest;
import demo.weblayer.restModel.CreateUserResponse;
import demo.weblayer.restModel.LoginRequest;
import demo.weblayer.restModel.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
public class UserWebController {
    private static final String ROOT_PATH = "/user";

    private final PayloadValidator payloadValidator;
    private final ServiceAdapter serviceAdapter;
    private final UserService userService;

    @Autowired
    public UserWebController(PayloadValidator payloadValidator,
                             ServiceAdapter serviceAdapter,
                             UserService userService) {
        this.payloadValidator = payloadValidator;
        this.serviceAdapter = serviceAdapter;
        this.userService = userService;
    }

    /**
     * POST /user/createUser : create new user
     * Creates a new user for given email and password
     * Server validates the input, creates the user, and
     * returns the identifier with the authentication token to be
     * used for subsequent requests.
     *
     * @param createUserRequest (required)
     * @return The new user has been created successfully. (status code 201)
     * or The user creation has failed due to invalid input. (status code 400)
     */
    @PostMapping(
            value = ROOT_PATH + "/createUser",
            produces = {"application/json"},
            consumes = {"application/json"})
    public ResponseEntity<CreateUserResponse> createUser(@RequestBody CreateUserRequest createUserRequest) {
        if (!payloadValidator.validateCreateUserRequest(createUserRequest))
            throw new UserException(UserException.UserExceptionTypes.INVALID_INPUT, "Invalid CreateUserRequest payload");

        String userEmail = createUserRequest.getUserEmail().toLowerCase(Locale.ROOT).trim();
        String userPassword = createUserRequest.getPassword();
        boolean userAdminState = createUserRequest.isAdmin();

        String userToken = (userAdminState)
                ? userService.createAdminUser(userEmail, userPassword)
                : userService.createManufacturerUser(userEmail, userPassword, serviceAdapter.mapManufacturer(createUserRequest.getManufacturer()));

        return new ResponseEntity<>(new CreateUserResponse(userToken), HttpStatus.CREATED);
    }

    /**
     * POST /user/login : user signs in
     * User signs in using email and password.
     * The system performs authentication and returns the
     * authentication token to be used for subsequent request.
     *
     * @param loginRequest (required)
     * @return The user has signed in successfully. (status code 200)
     * or The authentication has failed. (status code 401)
     */
    @PostMapping(
            value = ROOT_PATH + "/login",
            produces = {"application/json"},
            consumes = {"application/json"})
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        if (!payloadValidator.validateLoginRequest(loginRequest))
            throw new UserException(UserException.UserExceptionTypes.INVALID_INPUT, "Invalid LoginRequest payload");

        String userToken = userService.attemptLogin(loginRequest.getUserEmail().toLowerCase(Locale.ROOT).trim(), loginRequest.getPassword());
        return new ResponseEntity<>(new LoginResponse(userToken), HttpStatus.OK);
    }
}
