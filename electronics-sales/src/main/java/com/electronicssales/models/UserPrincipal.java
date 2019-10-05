package com.electronicssales.models;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.electronicssales.entities.User;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

@Data
public class UserPrincipal implements UserDetails {

    private static final long serialVersionUID = 1L;

    private String username;

    private String password;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean credentialsNonExpired;

    private boolean enabled;

    private String role;

    private Collection<? extends GrantedAuthority> authorities;

    public static UserPrincipal of(User user) {
        List<GrantedAuthority> authorities = Arrays.asList(
            new SimpleGrantedAuthority(user.getRole().toString())
        );

        UserPrincipal userPrincipal = new UserPrincipal();

        userPrincipal.setAuthorities(authorities);
        userPrincipal.setUsername(user.getUsername());
        userPrincipal.setPassword(user.getPassword());
        userPrincipal.setAccountNonExpired(user.isActived());
        userPrincipal.setAccountNonLocked(user.isActived());
        userPrincipal.setCredentialsNonExpired(user.isActived());
        userPrincipal.setEnabled(user.isActived());
        userPrincipal.setRole(user.getRole().toString());
        return userPrincipal;
    }
    
}