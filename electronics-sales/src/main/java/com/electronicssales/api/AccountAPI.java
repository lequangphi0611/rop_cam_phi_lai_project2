package com.electronicssales.api;

import javax.validation.Valid;

import com.electronicssales.entities.User;
import com.electronicssales.models.dtos.UserDto;
import com.electronicssales.models.responses.UserInfo;
import com.electronicssales.models.types.Role;
import com.electronicssales.services.UserService;
import com.electronicssales.utils.AuthenticateUtils;
import com.electronicssales.utils.Mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.Data;

@RestController
@RequestMapping("/api/accounts")
public class AccountAPI {

    @Autowired
    private UserService userService;

    @Autowired
    private Mapper<UserInfo, User> userInfoMapper;

    @GetMapping
    public ResponseEntity<?> fetchCurrentUserInfo() {
        String username = AuthenticateUtils.getUsernameAuthentecated();
        return ResponseEntity.ok(userService.getUserInfoByUsername(username));
    }

    @GetMapping("/roles")
    public ResponseEntity<?> fetchCurrentRole() {
        String role = AuthenticateUtils
            .getUserPrincipal()
            .getRole();
        return ResponseEntity
            .ok(new RoleResponse(role));
    }

    @PostMapping
    public ResponseEntity<?> createAdminAccount(@Valid @RequestBody UserDto userDto) {
        return ResponseEntity
            .created(null)
            .body(userInfoMapper
                .map(userService.createUser(userDto, Role.ADMIN))
            );
    }

    @Data
    @AllArgsConstructor
    static class RoleResponse {
        
        private String role;
        
    }
    
}