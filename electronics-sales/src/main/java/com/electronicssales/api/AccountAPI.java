package com.electronicssales.api;

import javax.servlet.http.HttpServletRequest;

import com.electronicssales.services.UserService;
import com.electronicssales.utils.AuthenticateUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
public class AccountAPI {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> fetchUserInfo(HttpServletRequest req) {
        String username = AuthenticateUtils.getUsernameAuthentecated();
        return ResponseEntity.ok(userService.getUserInfoByUsername(username));
    }
    
}