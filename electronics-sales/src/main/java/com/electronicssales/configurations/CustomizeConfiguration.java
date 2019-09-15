package com.electronicssales.configurations;

import com.electronicssales.models.dtos.UserDto;
import com.electronicssales.models.types.Role;
import com.electronicssales.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class CustomizeConfiguration {

    @Autowired UserService userService;

    @Autowired PasswordEncoder passwordEncoder;

    private UserDto getAdminInfo() {
        UserDto user = new UserDto();
        user.setActived(true);
        user.setPassword(passwordEncoder.encode("123"));
        user.setUsername("admin");
        user.setAddress("admin address");
        user.setFirstname("Admintrator");
        user.setLastname("Admintrator");
        user.setPhoneNumber("xxxxxxxxx");
        user.setRoleName(Role.ADMIN.toString());
        return user;
    }

    @Bean
    public void createAdminAccount(){
        new Thread(()-> {
            UserDto initUser = getAdminInfo();

            if(!userService.existByUsername(initUser.getUsername())) {
                userService.saveUser(initUser);
            }

        })
        .start();
    }

}