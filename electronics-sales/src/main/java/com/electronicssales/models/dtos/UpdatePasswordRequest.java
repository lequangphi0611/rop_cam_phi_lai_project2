package com.electronicssales.models.dtos;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdatePasswordRequest {

    private String oldPassword;

    private String newPassword;
    
}