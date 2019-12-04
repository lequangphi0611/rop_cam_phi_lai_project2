package com.electronicssales.models.responses;

import javax.validation.constraints.NotBlank;

import com.electronicssales.models.types.Role;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserInfoResponse extends RequiredUserInfoDto {

    private Long id;

    @NotBlank
    private String username;

    private Long avartarId;

    private Role role;

    private String password;
    
}