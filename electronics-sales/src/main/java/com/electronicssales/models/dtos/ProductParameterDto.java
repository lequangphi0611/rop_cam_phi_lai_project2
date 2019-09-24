package com.electronicssales.models.dtos;

import lombok.Data;

@Data
public class ProductParameterDto {

    private long id;

    private long parameterTypeId;

    private long productId;

    private String parameterValue;
    
}