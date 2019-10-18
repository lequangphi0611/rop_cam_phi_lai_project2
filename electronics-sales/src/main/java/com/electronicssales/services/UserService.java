package com.electronicssales.services;

import java.util.Optional;

import com.electronicssales.entities.User;
import com.electronicssales.models.dtos.UserDto;
import com.electronicssales.models.responses.UserInfoResponse;
import com.electronicssales.models.types.Role;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User createUser(UserDto userDto, Role role);

    User updateUser(UserDto userDto);

    boolean existByUsername(String username);

    boolean existsById(long userId);

    UserInfoResponse getUserInfoByUsername(String username);

    Optional<User> findByUsername(String username);
    
}