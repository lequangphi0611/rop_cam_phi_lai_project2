package com.electronicssales.models.dtos;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class ManufacturerDto {

    private long id;

    @NotBlank
    private String manufacturerName;

    private long logoId;
    
}