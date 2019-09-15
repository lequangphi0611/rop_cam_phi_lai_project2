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

    private long id;

    private String name;

    private String username;

    private String email;

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
        userPrincipal.setId(user.getId());
        StringBuilder fullname = new StringBuilder();
        if(user.getLastName() != null || user.getLastName() != "") {
            fullname
                .append(user.getLastName())
                .append(" ");
        }

        fullname.append(user.getFirstName());
        userPrincipal.setName(fullname.toString());
        userPrincipal.setUsername(user.getUsername());
        userPrincipal.setEmail(user.getEmail());
        userPrincipal.setPassword(user.getPassword());
        userPrincipal.setAccountNonExpired(user.isActived());
        userPrincipal.setAccountNonLocked(user.isActived());
        userPrincipal.setCredentialsNonExpired(user.isActived());
        userPrincipal.setEnabled(user.isActived());
        userPrincipal.setRole(user.getRole().toString());
        return userPrincipal;
    }
    
}