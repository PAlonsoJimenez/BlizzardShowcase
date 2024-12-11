package demo.servicelayer;

import demo.persistencelayer.DatabaseManager;
import demo.servicelayer.exceptions.UserException;
import demo.servicelayer.model.Manufacturer;
import demo.servicelayer.model.users.AdminUser;
import demo.servicelayer.model.users.ManufacturerUser;
import demo.servicelayer.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final DatabaseManager databaseManager;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(DatabaseManager databaseManager,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService) {
        this.databaseManager = databaseManager;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Create a new AdminUser with the userEmail
     * and userPassword arguments, if and
     * only if no other user with that userEmail
     * already exist
     *
     * @param userEmail    the new User email
     * @param userPassword the new User password
     * @return A new user Token if the credentials are valid
     * @throws UserException When the credentials are invalid
     */
    public String createAdminUser(String userEmail, String userPassword) {
        AdminUser newUser = new AdminUser(userEmail, passwordEncoder.encode(userPassword));
        boolean addedSuccessfully = databaseManager.addUser(newUser);
        if (addedSuccessfully) {
            return jwtService.generateToken(userEmail);
        } else {
            throw new UserException(UserException.UserExceptionTypes.INVALID_INPUT, "User already exist");
        }
    }

    /**
     * Create a new ManufacturerUser with the userEmail,
     * userPassword, and manufacturer arguments, if and
     * only if no other user with that userEmail
     * already exist and no other manufacturer with
     * that name already exist
     *
     * @param userEmail                    the new User email
     * @param userPassword                 the new User password
     * @param manufacturerUserManufacturer the new User manufacturer
     * @return A new user Token if the credentials are valid
     * @throws UserException When the credentials are invalid
     */
    public String createManufacturerUser(String userEmail, String userPassword, Manufacturer manufacturerUserManufacturer) {
        Optional<String> manufacturerIdOptional = databaseManager.storeManufacturer(manufacturerUserManufacturer);
        if (manufacturerIdOptional.isEmpty())
            throw new UserException(UserException.UserExceptionTypes.INVALID_INPUT, "A Manufacturer with that name already exist");

        String manufacturerId = manufacturerIdOptional.get();
        ManufacturerUser newUser = new ManufacturerUser(userEmail, passwordEncoder.encode(userPassword), manufacturerId);
        boolean manufacturerUserAddedSuccessfully = databaseManager.addUser(newUser);

        if (!manufacturerUserAddedSuccessfully) {
            databaseManager.deleteManufacturer(manufacturerId);
            throw new UserException(UserException.UserExceptionTypes.INVALID_INPUT, "User already exist");
        }

        return jwtService.generateToken(userEmail);
    }

    /**
     * Attempt to log in with given credentials
     * @param userEmail the user email
     * @param userPassword the user password
     * @return A new user Token if the credentials are valid
     * @throws UserException When the credentials are invalid
     */
    public String attemptLogin(String userEmail, String userPassword) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userEmail, userPassword);

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch (AuthenticationException authenticationException) {
            throw new UserException(UserException.UserExceptionTypes.BAD_AUTHENTICATION);
        }

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserException(UserException.UserExceptionTypes.BAD_AUTHENTICATION);
        }

        return jwtService.generateToken(userEmail);
    }
}
