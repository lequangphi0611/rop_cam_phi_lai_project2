package com.electronicssales.models.responses;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import com.electronicssales.models.types.Role;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserInfoResponse extends RequiredUserInfoDto {

    @NotBlank
    private String username;

    private Date birthday;

    private Long avartarId;

    private Role role;
    
}