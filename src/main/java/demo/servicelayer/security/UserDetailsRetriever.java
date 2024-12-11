package demo.servicelayer.security;
import demo.persistencelayer.DatabaseManager;
import demo.servicelayer.model.users.ManufacturerUser;
import demo.servicelayer.model.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsRetriever implements UserDetailsService {
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String ROLE_USER = "ROLE_USER";
    @Autowired
    private DatabaseManager databaseManager;

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        Optional<demo.servicelayer.model.users.User> optionalServiceUser = databaseManager.getUserByEmail(userEmail);

        if (optionalServiceUser.isEmpty()) throw new UsernameNotFoundException("User not found: " + userEmail);
        demo.servicelayer.model.users.User retrievedUser = optionalServiceUser.get();

        List<GrantedAuthority> userGrantedAuthorities = getUserAuthorities(retrievedUser);
        String manufacturerId = getManufacturerId(retrievedUser);

        return new UserDetailsImpl(retrievedUser.getUserName(), retrievedUser.getUserPassword(), manufacturerId, userGrantedAuthorities);
    }

    private List<GrantedAuthority> getUserAuthorities(demo.servicelayer.model.users.User retrievedUser) {
        List<GrantedAuthority> userGrantedAuthorities = new ArrayList<>();
        userGrantedAuthorities.add(new SimpleGrantedAuthority(ROLE_USER));
        if (retrievedUser.isAdmin()) {
            userGrantedAuthorities.add(new SimpleGrantedAuthority(ROLE_ADMIN));
        }

        return userGrantedAuthorities;
    }

    private String getManufacturerId(User retrievedUser) {
        return retrievedUser instanceof ManufacturerUser ? ((ManufacturerUser) retrievedUser).getManufacturerId() : null;
    }
}