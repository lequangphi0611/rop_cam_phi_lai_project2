package com.electronicssales.models.responses;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequiredUserInfoDto {

    private long userInfoId;

    @NotBlank
    private String firstname;

    private String lastname;

    @Pattern(regexp = "[0]([0-9]{9,11})")
    private String phoneNumber;

    @Pattern(regexp = "^[a-z][a-z0-9_\\.]+@[a-z0-9]{2,}(\\.[a-z0-9]{2,4}){1,2}$")
    private String email;

    private Date birthday;

    private String address;

    private boolean gender;
    
}