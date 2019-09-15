package com.electronicssales.services;

import com.electronicssales.entities.User;
import com.electronicssales.models.dtos.UserDto;
import com.electronicssales.models.responses.UserInfo;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User saveUser(UserDto userDto);

    boolean existByUsername(String username);

    UserInfo getUserInfoByUsername(String username);
    
}