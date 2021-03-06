package com.electronicssales.services;

import java.util.Optional;

import com.electronicssales.entities.User;
import com.electronicssales.models.UserProjections;
import com.electronicssales.models.dtos.UserDto;
import com.electronicssales.models.responses.UserInfoResponse;
import com.electronicssales.models.types.Role;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User createUser(UserDto userDto, Role role);

    User updateUser(UserDto userDto);

    boolean existByUsername(String username);

    boolean existsById(long userId);

    UserInfoResponse getUserInfoByUsername(String username);

    Optional<User> findByUsername(String username);

    Page<UserProjections> fetchEmployees(String search, Pageable pageable);

    void updateActived(long userId, boolean actived);

    void updatePasswordByUsername(String username, String password);

    boolean checkValidOldPassword(String username, String oldPassword);
    
}