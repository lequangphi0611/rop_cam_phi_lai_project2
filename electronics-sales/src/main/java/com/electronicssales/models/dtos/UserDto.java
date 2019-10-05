package com.electronicssales.models.dtos;

import com.electronicssales.models.responses.UserInfoResponse;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserDto extends UserInfoResponse {

    private String password;
        
}