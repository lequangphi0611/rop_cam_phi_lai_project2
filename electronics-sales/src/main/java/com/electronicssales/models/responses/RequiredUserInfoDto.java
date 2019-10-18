package com.electronicssales.models.responses;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class RequiredUserInfoDto {

    private long id;

    @NotBlank
    private String firstname;

    private String lastname;

    @Pattern(regexp = "[0]([0-9]{9,11})")
    private String phoneNumber;

    @Pattern(regexp = "^[a-z][a-z0-9_\\.]{5,32}@[a-z0-9]{2,}(\\.[a-z0-9]{2,4}){1,2}$")
    private String email;

    private String address;

    private boolean gender;
    
}