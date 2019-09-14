package com.electronicssales.services;

import com.electronicssales.entities.User;

public interface UserService {

    User saveUser(User user);

    User findByUsername(String username);

    boolean existByUsername(String username);
    
}