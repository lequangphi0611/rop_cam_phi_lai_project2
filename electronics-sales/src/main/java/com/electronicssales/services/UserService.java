package com.electronicssales.services;

import com.electronicssales.entities.User;
import com.electronicssales.models.dtos.UserDto;
import com.electronicssales.models.responses.UserInfo;
import com.electronicssales.models.types.Role;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User createUser(UserDto userDto, Role role);

    User saveUser(UserDto userDto);

    User updateUser(UserDto userDto);

    boolean existByUsername(String username);

    boolean existsById(long userId);

    UserInfo getUserInfoByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);
    
}