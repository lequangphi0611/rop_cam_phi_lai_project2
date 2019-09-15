package com.electronicssales.api;

import com.electronicssales.models.LoginRequest;
import com.electronicssales.models.UserPrincipal;
import com.electronicssales.models.responses.UserAuthenticationResponse;
import com.electronicssales.services.JwtTokenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationAPI {

    private static final String TOKEN_PREFIX = "Bearer";

    private static final long JWT_EXPIRATION_TIME = 604800000L;


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenService jwtTokenService;

    @PostMapping("/login")
    public ResponseEntity<UserAuthenticationResponse> login(
        @RequestBody LoginRequest loginRequest
    ) {
        Authentication authentication = authenticationManager
            .authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())    
            );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        String accessToken = new StringBuilder(TOKEN_PREFIX)
            .append(" ")
            .append(
                jwtTokenService
                    .setJwtExpirationTime(JWT_EXPIRATION_TIME)
                    .generate(userPrincipal.getUsername())
            )
            .toString();
        return ResponseEntity
            .ok(new UserAuthenticationResponse(
                accessToken, 
                userPrincipal.getUsername(), 
                userPrincipal.getRole()
            ));
    }
    
}