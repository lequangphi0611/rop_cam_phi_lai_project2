package com.electronicssales.utils;

import com.electronicssales.models.UserPrincipal;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticateUtils {

    public static String getUsernameAuthentecated() {
        return getUserPrincipal().getUsername();
    }

    public static UserPrincipal getUserPrincipal() {
        return (UserPrincipal) getSecurityContext()
            .getAuthentication()
            .getPrincipal();
    }

    public static SecurityContext getSecurityContext() {
        return SecurityContextHolder.getContext();
    }

    public static void setAuthentication(Authentication authentication) {
        getSecurityContext().setAuthentication(authentication);
    }

    public static void clear() {
        setAuthentication(null);
    }

}