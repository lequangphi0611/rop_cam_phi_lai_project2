package com.electronicssales.resources;

import java.util.concurrent.Callable;

import com.electronicssales.models.responses.RequiredUserInfoDto;
import com.electronicssales.services.UserInfoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user-infos")
public class UserInfoResource {

    @Lazy
    @Autowired
    private UserInfoService userInfoService;

    @PostMapping
    public Callable<ResponseEntity<?>> create(@RequestBody RequiredUserInfoDto requiredUserInfoDto) {
        return () -> ResponseEntity.created(null).body(userInfoService.save(requiredUserInfoDto));
    }
    
    @PutMapping("/{id}")
    public Callable<ResponseEntity<?>> update(@RequestBody RequiredUserInfoDto requiredUserInfoDto
        , @PathVariable long id) {
        requiredUserInfoDto.setUserInfoId(id);
        return () -> ResponseEntity.ok(userInfoService.save(requiredUserInfoDto));
    }
}