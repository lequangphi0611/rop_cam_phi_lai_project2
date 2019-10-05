package com.electronicssales.resources;

import javax.validation.Valid;

import com.electronicssales.entities.User;
import com.electronicssales.models.LoginRequest;
import com.electronicssales.models.UserPrincipal;
import com.electronicssales.models.dtos.UserDto;
import com.electronicssales.models.responses.UserAuthenticationResponse;
import com.electronicssales.models.responses.UserInfoResponse;
import com.electronicssales.models.types.Role;
import com.electronicssales.services.JwtTokenService;
import com.electronicssales.services.UserService;
import com.electronicssales.utils.Mapper;

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
@RequestMapping("/api")
public class AuthenticationResource {

    private static final String TOKEN_PREFIX = "Bearer";

    private static final long JWT_EXPIRATION_TIME = 604800000L;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private UserService userService;

    @Autowired
    private Mapper<UserInfoResponse, User> userInfoMapper;

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
                userPrincipal.getUsername()
            ));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserDto userDto) {
        User userSaved = userService.createUser(userDto, Role.CUSTOMER);
        return ResponseEntity
            .created(null)
            .body(userInfoMapper.mapping(userSaved));
    }
    
}