package com.electronicssales.resources;

import java.util.Optional;

import javax.validation.Valid;

import com.electronicssales.entities.User;
import com.electronicssales.models.dtos.UserDto;
import com.electronicssales.models.responses.UserInfoResponse;
import com.electronicssales.models.types.Role;
import com.electronicssales.services.UserService;
import com.electronicssales.utils.AuthenticateUtils;
import com.electronicssales.utils.Mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.Data;

@RestController
@RequestMapping("/api/accounts")
public class AccountResource {

    @Lazy
    @Autowired
    private UserService userService;

    @Lazy
    @Autowired
    private Mapper<UserInfoResponse, User> userInfoMapper;

    @GetMapping("/me")
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
                .mapping(userService.createUser(userDto, Role.EMPLOYEE))
            );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAccount(
        @RequestBody @Valid UserDto userDto,
        @PathVariable long id
    ) 
    {
        userDto.setId(id);
        UserInfoResponse result = Optional
            .of(userDto)
            .map(userService::updateUser)
            .map(userInfoMapper::mapping)
            .get();
        return ResponseEntity.ok(result);
    }

    @Data
    @AllArgsConstructor
    static class RoleResponse {
        
        private String role;
        
    }
    
}