package com.electronicssales.resources;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.Callable;

import com.electronicssales.entities.User;
import com.electronicssales.entities.UserInfo;
import com.electronicssales.models.UserPrincipal;
import com.electronicssales.models.dtos.UserDto;
import com.electronicssales.models.responses.UserInfoResponse;
import com.electronicssales.models.types.Role;
import com.electronicssales.services.UserService;
import com.electronicssales.utils.AuthenticateUtils;
import com.electronicssales.utils.Mapper;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
            UserPrincipal userPrincipal = AuthenticateUtils.getUserPrincipal();
            System.out.println(AuthenticateUtils.getSecurityContext().getAuthentication().getDetails());
            String username = userPrincipal.getUsername();
            UserInfoResponse user = userService.getUserInfoByUsername(username);
            user.setPassword(userPrincipal.getPassword());
            return ResponseEntity.ok(user);
        };
    }

    @GetMapping("/employees")
    public Callable<ResponseEntity<?>> fechEmployees(
        @RequestParam(value = "search", required = false)
        String search,
        Pageable pageable
    ) {
        return () -> ResponseEntity.ok(this.userService.fetchEmployees(search, pageable));
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
            if (userService.existByUsername(username)) {
                return ResponseEntity.ok().build();
            }

            return ResponseEntity.notFound().build();
        };
    }

    @PostMapping(consumes = { "multipart/form-data" })
    public Callable<ResponseEntity<?>> createAdminAccount(@RequestParam("user") String userDtoStr,
            @RequestParam(value = "avartar", required = false) MultipartFile avartar)
            throws JsonParseException, JsonMappingException, IOException {
        UserDto userDto = this.objectMapper.readValue(userDtoStr, UserDto.class);
        Optional.ofNullable(avartar).ifPresent(userDto::setAvartar);
        return () -> ResponseEntity.created(null)
            .body(userInfoMapper.mapping(userService.createUser(userDto, Role.EMPLOYEE)));
    }

    @PutMapping(value = "/{id}", consumes = { "multipart/form-data" })
    public Callable<ResponseEntity<?>> updateAccount(@RequestParam("user") String userDtoStr, @PathVariable long id,
            @RequestParam(value = "avartar", required = false) MultipartFile avartar) {
        return () -> {
            UserDto userDto = this.objectMapper.readValue(userDtoStr, UserDto.class);
            userDto.setId(id);
            Optional.ofNullable(avartar).ifPresent(userDto::setAvartar);
            UserInfoResponse result = Optional.of(userDto).map(userService::updateUser).map(userInfoMapper::mapping)
                    .get();
            return ResponseEntity.ok(result);
        };
    }

    @DeleteMapping("{id}")
    public Callable<ResponseEntity<?>> removeAccount(@PathVariable long id) {
        return () -> {
            this.userService.updateActived(id, false);
            return ResponseEntity.ok().build();
        };
    }

    @Data
    @AllArgsConstructor
    static class RoleResponse {

        private String role;

    }

}