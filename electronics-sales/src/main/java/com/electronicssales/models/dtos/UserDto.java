package com.electronicssales.models.dtos;

import com.electronicssales.models.responses.UserInfoResponse;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserDto extends UserInfoResponse {

    private String password;

    private MultipartFile avartar;
        
}