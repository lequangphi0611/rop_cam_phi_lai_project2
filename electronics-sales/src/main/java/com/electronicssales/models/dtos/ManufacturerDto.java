package com.electronicssales.models.dtos;

import javax.validation.constraints.NotBlank;

import org.springframework.web.multipart.MultipartFile;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ManufacturerDto {

    long id;

    @NotBlank
    String manufacturerName;

    MultipartFile image;

    Long categoryId; 
    
}