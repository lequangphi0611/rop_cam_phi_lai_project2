package com.electronicssales.models.responses;

import lombok.Data;

@Data
public class ProductParameterResponse {

    private long Id;

    private String parameterType;

    private String parameterValue;
    
}