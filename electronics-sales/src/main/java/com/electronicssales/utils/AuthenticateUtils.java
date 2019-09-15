package com.electronicssales.utils;

import com.electronicssales.models.UserPrincipal;

import org.springframework.security.core.context.SecurityContextHolder;

/**
 * AuthenticateUtils
 */
public class AuthenticateUtils {

    public static String getUsernameAuthentecated() {
        return ((UserPrincipal) SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getPrincipal())
            .getUsername();
    }
}