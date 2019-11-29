package com.electronicssales.resources;

import java.util.concurrent.Callable;

import javax.validation.Valid;

import com.electronicssales.entities.User;
import com.electronicssales.models.LoginRequest;
import com.electronicssales.models.UserPrincipal;
import com.electronicssales.models.dtos.UserDto;
import com.electronicssales.models.responses.UserAuthenticationResponse;
import com.electronicssales.models.types.Role;
import com.electronicssales.services.JwtTokenService;
import com.electronicssales.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class AuthenticationResource {

    private static final long JWT_EXPIRATION_TIME = 604800000L;

    @Lazy
    @Autowired
    private AuthenticationManager authenticationManager;

    @Lazy
    @Autowired
    private JwtTokenService jwtTokenService;

    @Lazy
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Callable<ResponseEntity<?>> login(
        @RequestBody LoginRequest loginRequest
    ) {
        return () -> login(loginRequest.getUsername(), loginRequest.getPassword());
    }

    @PostMapping("/register")
    public Callable<ResponseEntity<?>> register(@RequestBody @Valid UserDto userDto) {
        return () -> {
            User userSaved = userService.createUser(userDto, Role.CUSTOMER);
            return login(userSaved.getUsername(), userDto.getPassword());
        };
    }

    private ResponseEntity<?> login(String username, String password) {
        Authentication authentication = authenticationManager
            .authenticate(
                new UsernamePasswordAuthenticationToken(username, password)    
            );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        String accessToken = jwtTokenService
                    .setJwtExpirationTime(JWT_EXPIRATION_TIME)
                    .generate(userPrincipal.getUsername());
        return ResponseEntity
            .ok(new UserAuthenticationResponse(
                accessToken, 
                userPrincipal.getUsername(),
                userPrincipal.getRole()
            ));
    }
    
}