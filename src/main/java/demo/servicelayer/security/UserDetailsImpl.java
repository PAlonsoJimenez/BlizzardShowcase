package demo.servicelayer.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserDetailsImpl implements UserDetails {
    private final String userEmail;
    private final String password;
    private final String manufacturerId;
    private final List<GrantedAuthority> authorities;

    public UserDetailsImpl(String userEmail, String password, String manufacturerId, List<GrantedAuthority> authorities) {
        this.userEmail = userEmail;
        this.password = password;
        this.manufacturerId = manufacturerId;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userEmail;
    }

    public String getManufacturerId() {
        return manufacturerId;
    }
}
