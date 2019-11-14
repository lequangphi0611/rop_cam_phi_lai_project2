package com.electronicssales.resources;

import java.util.Optional;
import java.util.concurrent.Callable;

import javax.validation.Valid;

import com.electronicssales.entities.User;
import com.electronicssales.models.dtos.UserDto;
import com.electronicssales.models.responses.UserInfoResponse;
import com.electronicssales.models.types.Role;
import com.electronicssales.services.UserService;
import com.electronicssales.utils.AuthenticateUtils;
import com.electronicssales.utils.Mapper;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

    @Lazy
    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/current")
    public Callable<ResponseEntity<?>> fetchCurrentUserInfo() {
        return () -> {
            String username = AuthenticateUtils.getUsernameAuthentecated();
            return ResponseEntity.ok(userService.getUserInfoByUsername(username));
        };
    }

    @GetMapping("/roles")
    public Callable<ResponseEntity<?>> fetchCurrentRole() {
        return () -> {
            String role = AuthenticateUtils.getUserPrincipal().getRole();
            return ResponseEntity.ok(new RoleResponse(role));
        };
    }

    @RequestMapping(value = "/username/{username}", method = RequestMethod.HEAD)
    public Callable<ResponseEntity<?>> existsByUsername(@PathVariable String username) {
        return () -> {
            if(userService.existByUsername(username)) {
                return ResponseEntity.ok().build();
            }

            return ResponseEntity.notFound().build();
        };
    }

    @PostMapping(consumes = { "multipart/form-data" })
    public Callable<ResponseEntity<?>> createAdminAccount(@Valid @RequestBody UserDto userDto) {
        return () -> ResponseEntity.created(null)
            .body(userInfoMapper.mapping(userService.createUser(userDto, Role.EMPLOYEE)));
    }

    @PutMapping(value = "/{id}", consumes = { "multipart/form-data" })
    public Callable<ResponseEntity<?>> updateAccount(@RequestParam("user") String userDtoStr, @PathVariable long id, @RequestParam(value = "avartar", required = false) MultipartFile avartar) {
        return () -> {
            UserDto userDto = this.objectMapper.readValue(userDtoStr, UserDto.class);
            userDto.setId(id);
            Optional.ofNullable(avartar).ifPresent(userDto::setAvartar);
            UserInfoResponse result = Optional.of(userDto).map(userService::updateUser).map(userInfoMapper::mapping).get();
            return ResponseEntity.ok(result);
        };
    }

    @Data
    @AllArgsConstructor
    static class RoleResponse {

        private String role;

    }

}