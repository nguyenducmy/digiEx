package com.supertomato.restaurant.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.supertomato.restaurant.common.enums.UserRole;
import com.supertomato.restaurant.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author DiGiEx
 */
public class AuthUser implements UserDetails {

    @Getter
    private final String id;
    private final String username;
    private final String password;
    @Getter
    private String email;
    private final boolean enabled;
    @Getter
    private UserRole role;

    @Getter
    private boolean isUserRoot;

    public AuthUser(User user) {
        this.id = user.getId();
        this.username = user.getFirstName() + " " + user.getLastName();
        this.password = "";
        this.email = user.getEmail();
        this.role = user.getUserRole();
        this.isUserRoot = user.isUserRoot();
        this.enabled = true;
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return username;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
