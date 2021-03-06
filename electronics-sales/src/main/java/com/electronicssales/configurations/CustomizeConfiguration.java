package com.electronicssales.configurations;

import java.util.Date;

import javax.persistence.EntityExistsException;

import com.electronicssales.models.dtos.UserDto;
import com.electronicssales.models.types.Role;
import com.electronicssales.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class CustomizeConfiguration {

    @Autowired 
    UserService userService;

    private UserDto getAdminInfo() {
        UserDto user = new UserDto();
        user.setPassword("admin");
        user.setUsername("admin");
        user.setAddress("admin address");
        user.setFirstname("Admintrator");
        user.setLastname("Admintrator");
        user.setPhoneNumber("012345678");
        user.setEmail("admintrator@gmail.com");
        user.setBirthday(new Date());
        return user;
    }

    @Bean
    public void createAdminAccount(){
        new Thread(()-> {
            UserDto initUser = getAdminInfo();

            try {
                userService.createUser(initUser, Role.MANAGER);
            } catch (EntityExistsException e) {
            }

        })
        .start();
    }

}