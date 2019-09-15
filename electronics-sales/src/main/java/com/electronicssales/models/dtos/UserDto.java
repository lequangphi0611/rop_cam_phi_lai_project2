package com.electronicssales.models.dtos;

import com.electronicssales.models.responses.UserInfo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserDto extends UserInfo {

    private String username;

    private String password;

    private boolean actived = true;

    private String roleName;
    
}