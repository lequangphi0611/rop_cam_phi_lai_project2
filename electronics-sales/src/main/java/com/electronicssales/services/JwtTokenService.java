package com.electronicssales.services;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;

public interface JwtTokenService {

    JwtTokenService setJwtExpirationTime(long jwtExpirationTime);

    String generate(String username);

    String generate(Authentication authentication);

    boolean validateToken(String authToken);

    String getUsernameFromToken(String token);

    String getUsernameFromRequest(HttpServletRequest req);

    String getJwtTokenFrom(HttpServletRequest req);

}