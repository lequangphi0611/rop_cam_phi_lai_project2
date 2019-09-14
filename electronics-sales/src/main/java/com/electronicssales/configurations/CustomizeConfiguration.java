package com.electronicssales.configurations;

import java.util.Arrays;

import com.electronicssales.entities.Role;
import com.electronicssales.entities.User;
import com.electronicssales.repositories.RoleRepository;
import com.electronicssales.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomizeConfiguration {

    @Autowired UserService userService;

    @Autowired RoleRepository roleRepository;

    private static final String ADMIN_ROLE = "ADMIN";

    private static final String CUSTOMER_ROLE = "CUSTOMER";

    private void initAndSaveRole() {
        if(roleRepository.count() > 0) {
            return;
        }

        Role admin = new Role();
        admin.setRoleName(ADMIN_ROLE);

        Role customer = new Role();
        customer.setRoleName(CUSTOMER_ROLE);

        roleRepository.saveAll(Arrays.asList(admin, customer));
    }

    private User getAdminInfo() {
        User user = new User();
        user.setActived(true);
        user.setPassword("123");
        user.setUsername("admin");
        user.setAddress("admin address");
        user.setFirstName("Admintrator");
        user.setPhoneNumber("xxxxxxxxx");
        
        return user;
    }

    @Bean
    public void createAdminAccount(){
        new Thread(()-> {
            User initUser = getAdminInfo();

            if(userService.existByUsername(initUser.getUsername())) {
                return; 
            }

            initAndSaveRole();
            Role role = roleRepository.findByRoleName(ADMIN_ROLE);
            initUser.setRole(role);
            userService.saveUser(initUser);
        })
        .start();
    }

}